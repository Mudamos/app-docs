//
//  ViewController.swift
//  MudamosExample
//
//  Created by Guilherme da Silva Mello on 14/02/18.
//  Copyright © 2018 Tagview. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
  @IBOutlet weak var resultView: UITextView!

  override func viewDidLoad() {
    super.viewDidLoad()
    // Do any additional setup after loading the view, typically from a nib.
  }

  @IBAction func signMessage(_ sender: Any) {
    let messageToSign = String.random(length: 32)
    resultView.text = "Resultado: Vamos assinar a mensagem '\(messageToSign)'"

    let payload = ["message": messageToSign] as [String : Any]

    let item = NSExtensionItem()
    let attachment = NSItemProvider(item: payload as NSDictionary, typeIdentifier: Mudamos.payloadIdentifier.rawValue)
    item.attachments = [attachment]

    let vc = UIActivityViewController(activityItems: [item], applicationActivities: nil)
    vc.completionWithItemsHandler = { activityType, success, items, error in
      if !success {
        self.resultView.text = "Cancelado"
        return
      }

      if error != nil {
        self.resultView.text = "Erro: \(error!)"
        return
      }

      guard let item = items?.last as? NSExtensionItem,
        let attachment = item.attachments?.last as? NSItemProvider else {
        self.resultView.text = "Nenhum conteúdo retornado"
        return
      }

      if !attachment.hasItemConformingToTypeIdentifier(Mudamos.resultIdentifier.rawValue) {
        self.resultView.text = "Nenhum conteúdo retornado"
        return
      }

      attachment.loadItem(forTypeIdentifier: Mudamos.resultIdentifier.rawValue, options: nil) { result, error in
        OperationQueue.main.addOperation {
          if error != nil {
            self.resultView.text = "Erro: \(error!)"
            return
          }

          guard let parsedResult = result as? [String: Any] else {
            self.resultView.text = "Erro ao converter resultado"
            return
          }

          let err = parsedResult["error"] as? Bool ?? true
          self.resultView.text! += "\nErro: \(err)"

          if let message = parsedResult["message"] as? String {
            self.resultView.text! += "\nMensagem: \(message)"
          }

          if let publicKey = parsedResult["publicKey"] as? String {
            self.resultView.text! += "\nChave pública: \(publicKey)"
          }

          if let signature = parsedResult["signature"] as? String {
            self.resultView.text! += "\nAssinatura: \(signature)"
          }

          if let timestamp = parsedResult["timestamp"] as? String {
            self.resultView.text! += "\nTimestamp: \(timestamp)"
          }
        }
      }
    }

    vc.popoverPresentationController?.sourceView = view
    present(vc, animated: true, completion: nil)
  }
}
