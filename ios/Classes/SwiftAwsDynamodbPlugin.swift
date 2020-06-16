import Flutter
import UIKit
import AWSMobileClient
import AWSDynamoDB

public class SwiftAwsDynamodbPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "aws_dynamodb", binaryMessenger: registrar.messenger())
    let instance = SwiftAwsDynamodbPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch (call.method) {
      case "initialize":
        initialize(call, result)
        break;
      case "query":
        query(call, result)
        break;
      case "batchGet":
        batchGet(call, result)
        break;
      case "putItem":
        putItem(call, result)
        break;
      case "deleteItem":
        deleteItem(call, result)
        break;
      default:
      result(FlutterMethodNotImplemented)
    }
  }

  func initialize(_ call: FlutterMethodCall,_ result: @escaping FlutterResult) {
    AWSMobileClient.sharedInstance().initialize { (userState, error) in
      if let userState = userState {
        result(["result": userState.rawValue]);
      } else if let error = error {
        result(["error": error.localizedDescription])
      }
    }
    
    AWSServiceManager.default()?.defaultServiceConfiguration = AWSServiceConfiguration(region: .USEast2, credentialsProvider: AWSMobileClient.sharedInstance().getCredentialsProvider())
  }

  func query(_ call: FlutterMethodCall,_ result: @escaping FlutterResult) {
    let args = call.arguments as? NSDictionary
        
    let tableName: String = (args?.value(forKey: "table_name") as? String) ?? ""
    let keyAttributeTitle: String = (args?.value(forKey: "key_attribute_title") as? String) ?? ""
    let keyAttributeValue: Any = args?.value(forKey: "key_attribute_value") ?? ""
    
    let keyAttribute = AWSDynamoDBAttributeValue()
    if let value = keyAttributeValue as? String {
        if let _ = Double(value) {
            keyAttribute?.n = value
        } else {
            keyAttribute?.s = value
        }
    }
    
    let dynamoDb = AWSDynamoDB.default()
    
    let input = AWSDynamoDBQueryInput()
    input?.tableName = tableName
    input?.keyConditionExpression = "#p = :keyVal"
    input?.expressionAttributeNames = [
        "#p": keyAttributeTitle
    ]
    input?.expressionAttributeValues = [
        ":keyVal": keyAttribute!
    ]
    
    dynamoDb.query(input!) { (output, error) in
        DispatchQueue.main.async {
            if let error = error {
                print(error)
                result(FlutterError(code: "0", message: error.localizedDescription, details: nil))
            } else if let output = output, let items = output.items {
                result(self.parseOutput(items: items))
            } else {
                print("No error or output")
                result(nil)
            }
        }
    }
  }
    
    func batchGet(_ call: FlutterMethodCall,_ result: @escaping FlutterResult) {
        let args = call.arguments as? NSDictionary
            
        let tableName: String = (args?.value(forKey: "table_name") as? String) ?? ""
        let keys: [String] = (args?.value(forKey: "keys") as? [String]) ?? [String]()
        if (keys.count == 0) {
            result(nil)
            return
        }
        let keyAttributeTitle: String = (args?.value(forKey: "key_attribute_title") as? String) ?? ""
        var attributeKeys = [[String:AWSDynamoDBAttributeValue]]()
        for key in keys {
            let attribute = AWSDynamoDBAttributeValue()
            attribute?.s = key
            attributeKeys.append([keyAttributeTitle : attribute!])
        }
        
        let map = AWSDynamoDBKeysAndAttributes()
        map?.keys = attributeKeys
        
        let dynamoDb = AWSDynamoDB.default()
        
        let input = AWSDynamoDBBatchGetItemInput();
        
        input?.requestItems = [
            tableName: map!
        ]
        
        dynamoDb.batchGetItem(input!) { (output, error) in
            DispatchQueue.main.async {
                if let error = error {
                    print(error)
                    result(FlutterError(code: "0", message: error.localizedDescription, details: nil))
                } else if let output = output, let responses = output.responses, let items = responses[tableName] {
                    result(self.parseOutput(items: items))
                } else {
                    print("No error or output")
                    result(nil)
                }
            }
        }
    }

  func parseOutput(items: [[String:AWSDynamoDBAttributeValue]]) -> Array<Dictionary<String, Any>> {
    var array = [Dictionary<String, Any>]()
    for item in items {
      var dict: [String:Any] = [:]
      for key in item.keys {
        guard let value = item[key] else {
            print("no value for key")
            return [Dictionary<String, Any>]()
        }
        dict.updateValue(parseAttribute(value), forKey: key)
      }
      array.append(dict)
    }
    
    return array
  }

  func parseAttribute(_ attribute: AWSDynamoDBAttributeValue) -> Any {
    if let value = attribute.s {
      return value
    } else if let value = attribute.n, let numValue = Double(value) {
      return numValue
    } else if let value = attribute.l {
      var array = [Any]()
      for item in value {
        array.append(parseAttribute(item))
      }
      return array
    } else {
      return "unknown"
    }
  }

  func putItem(_ call: FlutterMethodCall,_ result: @escaping FlutterResult) {
    let args = call.arguments as? NSDictionary
          
    let tableName: String = (args?.value(forKey: "table_name") as? String) ?? ""
    let item: Dictionary<String, Any> = (args?.value(forKey: "item") as? Dictionary<String, Any>) ?? [:]
    if (item.count == 0) {
      print("no item attributes found")
      result(false);
      return;
    }

    let dynamoDb = AWSDynamoDB.default()

    var itemInput: Dictionary<String, AWSDynamoDBAttributeValue> = [:]
    for key in item.keys {
      guard let value = item[key] else {
          print("no value for key")
          result(false)
          return;
      }
      let attribute = AWSDynamoDBAttributeValue()
      attribute?.s = value as? String ?? ""
      itemInput.updateValue(attribute!, forKey: key)
    }

    let input = AWSDynamoDBPutItemInput();
    input?.tableName = tableName;
    input?.item = itemInput

    dynamoDb.putItem(input!) { (output, error) in
       DispatchQueue.main.async {
        if let error = error {
          print(error)
          result(FlutterError(code: "0", message: error.localizedDescription, details: nil))
        } else if let _ = output {
          result(true)
        } else {
          print("No error or output")
          result(false)
        }
      }
    }
  }

  func deleteItem(_ call: FlutterMethodCall,_ result: @escaping FlutterResult) {
    let args = call.arguments as? NSDictionary
          
    let tableName: String = (args?.value(forKey: "table_name") as? String) ?? ""
    let item: Dictionary<String, Any> = (args?.value(forKey: "item") as? Dictionary<String, Any>) ?? [:]
    if (item.count == 0) {
      print("no item attributes found")
      result(false);
      return;
    }

    let dynamoDb = AWSDynamoDB.default()

    var itemInput: Dictionary<String, AWSDynamoDBAttributeValue> = [:]
    for key in item.keys {
      guard let value = item[key] else {
          print("no value for key")
          result(false)
          return;
      }
      let attribute = AWSDynamoDBAttributeValue()
      attribute?.s = value as? String ?? ""
      itemInput.updateValue(attribute!, forKey: key)
    }

    let input = AWSDynamoDBDeleteItemInput();
    input?.tableName = tableName;
    input?.key = itemInput

    dynamoDb.deleteItem(input!) { (output, error) in
       DispatchQueue.main.async {
        if let error = error {
          print(error)
          result(FlutterError(code: "0", message: error.localizedDescription, details: nil))
        } else if let _ = output {
          result(true)
        } else {
          print("No error or output")
          result(false)
        }
      }
    }
  }
}
