//
//  StringExtension.swift
//  MudamosExample
//
//  Created by Guilherme da Silva Mello on 14/02/18.
//  Copyright © 2018 Tagview. All rights reserved.
//

import Foundation

extension String {
  static func random(length: Int = 16) -> String {
    let letters: NSString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    let len = UInt32(letters.length)
    var randomString = ""

    for _ in 0 ..< length {
      let rand = arc4random_uniform(len)
      var nextChar = letters.character(at: Int(rand))
      randomString += NSString(characters: &nextChar, length: 1) as String
    }

    return randomString
  }
}
