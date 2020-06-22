import 'dart:async';

import 'package:flutter/services.dart';

class AwsDynamodb {
  static const MethodChannel _channel = const MethodChannel('aws_dynamodb');

  static Future<void> initialize() async {
    await _channel.invokeMethod("initialize");
  }

  static Future<List<Map>> query({
    String tableName,
    String keyAttributeTitle,
    dynamic keyAttributeValue,
  }) async {
    print("querying!");
    var value = await _channel.invokeListMethod<Map>("query", {
      "table_name": tableName,
      "key_attribute_title": keyAttributeTitle,
      "key_attribute_value": keyAttributeValue,
    });
    print(value);

    return value;
  }

  static Future<List<Map>> batchGet({
    String tableName,
    String keyAttributeTitle,
    List<String> keys,
  }) async {
    var value = await _channel.invokeListMethod<Map>("batchGet", {
      "table_name": tableName,
      "key_attribute_title": keyAttributeTitle,
      "keys": keys,
    });

    return value;
  }

  static Future<bool> putItem({
    String tableName,
    Map item,
  }) async {
    var value = await _channel.invokeMethod<bool>("putItem", {
      "table_name": tableName,
      "item": item,
    });

    return value;
  }

  static Future<bool> deleteItem({
    String tableName,
    Map item,
  }) async {
    var value = await _channel.invokeMethod<bool>("deleteItem", {
      "table_name": tableName,
      "item": item,
    });

    return value;
  }
}
