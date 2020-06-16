package com.example.aws_dynamodb

import androidx.annotation.NonNull;
import android.app.Activity

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails

/** AwsDynamodbPlugin */
public class AwsDynamodbPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  var activity : Activity? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "aws_dynamodb")
    channel.setMethodCallHandler(this);
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "aws_dynamodb")
      val activity = registrar.activity()
      channel.setMethodCallHandler(AwsDynamodbPlugin())
    }
  }

  override fun onAttachedToActivity(activityPluginBinding : ActivityPluginBinding) {
    activity = activityPluginBinding.getActivity()
  }

  override fun onDetachedFromActivityForConfigChanges() {
    
  }

  override fun onReattachedToActivityForConfigChanges(activityPluginBinding : ActivityPluginBinding) {
    activity = activityPluginBinding.getActivity()
  }

  override fun onDetachedFromActivity() {

  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "initialize") {
      initialize(call, result)
    } else if (call.method == "query") {
      query(call, result)
    } else if (call.method == "batchGet") {
      batchGet(call, result)
    } else if (call.method == "putItem") {
      putItem(call, result)
    } else if (call.method == "deleteItem") {
      deleteItem(call, result)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  fun initialize(@NonNull call: MethodCall, @NonNull result: Result) {
    AWSMobileClient.getInstance().initialize(activity, object: Callback<UserStateDetails> {
      override fun onResult(userStateDetails: UserStateDetails) {
        val userState = userStateDetails.getUserState().toString()
        val map = HashMap<String,String>()
        map.put("result", userState)
        activity!!.runOnUiThread(java.lang.Runnable {
          result.success(map)
        })
      }

      override fun onError(e: Exception) {
        val map = HashMap<String,String>()
        map.put("error", e.toString())
        activity!!.runOnUiThread(java.lang.Runnable {
          result.success(map)
        })
      }
    })

    // AWSServiceManager.default()?.defaultServiceConfiguration = AWSServiceConfiguration(region: .USEast2, credentialsProvider: AWSMobileClient.sharedInstance().getCredentialsProvider())
  }

  fun query(@NonNull call: MethodCall, @NonNull result: Result) {
    // let args = call.arguments as? NSDictionary
        
    // let tableName: String = (args?.value(forKey: "table_name") as? String) ?? ""
    // let keyAttributeTitle: String = (args?.value(forKey: "key_attribute_title") as? String) ?? ""
    // let keyAttributeValue: Any = args?.value(forKey: "key_attribute_value") ?? ""
    
    // let keyAttribute = AWSDynamoDBAttributeValue()
    // if let value = keyAttributeValue as? String {
    //     if let _ = Double(value) {
    //         keyAttribute?.n = value
    //     } else {
    //         keyAttribute?.s = value
    //     }
    // }
    
    // let dynamoDb = AWSDynamoDB.default()
    
    // let input = AWSDynamoDBQueryInput()
    // input?.tableName = tableName
    // input?.keyConditionExpression = "#p = :keyVal"
    // input?.expressionAttributeNames = [
    //     "#p": keyAttributeTitle
    // ]
    // input?.expressionAttributeValues = [
    //     ":keyVal": keyAttribute!
    // ]
    
    // dynamoDb.query(input!) { (output, error) in
    //     DispatchQueue.main.async {
    //         if let error = error {
    //             print(error)
    //             result(FlutterError(code: "0", message: error.localizedDescription, details: nil))
    //         } else if let output = output, let items = output.items {
    //             result(self.parseOutput(items: items))
    //         } else {
    //             print("No error or output")
    //             result(nil)
    //         }
    //     }
    // }
  }

  fun batchGet(@NonNull call: MethodCall, @NonNull result: Result) {
    // let args = call.arguments as? NSDictionary
            
    // let tableName: String = (args?.value(forKey: "table_name") as? String) ?? ""
    // let keys: [String] = (args?.value(forKey: "keys") as? [String]) ?? [String]()
    // if (keys.count == 0) {
    //     result(nil)
    //     return
    // }
    // let keyAttributeTitle: String = (args?.value(forKey: "key_attribute_title") as? String) ?? ""
    // var attributeKeys = [[String:AWSDynamoDBAttributeValue]]()
    // for key in keys {
    //     let attribute = AWSDynamoDBAttributeValue()
    //     attribute?.s = key
    //     attributeKeys.append([keyAttributeTitle : attribute!])
    // }
    
    // let map = AWSDynamoDBKeysAndAttributes()
    // map?.keys = attributeKeys
    
    // let dynamoDb = AWSDynamoDB.default()
    
    // let input = AWSDynamoDBBatchGetItemInput();
    
    // input?.requestItems = [
    //     tableName: map!
    // ]
    
    // dynamoDb.batchGetItem(input!) { (output, error) in
    //     DispatchQueue.main.async {
    //         if let error = error {
    //             print(error)
    //             result(FlutterError(code: "0", message: error.localizedDescription, details: nil))
    //         } else if let output = output, let responses = output.responses, let items = responses[tableName] {
    //             result(self.parseOutput(items: items))
    //         } else {
    //             print("No error or output")
    //             result(nil)
    //         }
    //     }
    // }
  }

  fun putItem(@NonNull call: MethodCall, @NonNull result: Result) {
    // let args = call.arguments as? NSDictionary
          
    // let tableName: String = (args?.value(forKey: "table_name") as? String) ?? ""
    // let item: Dictionary<String, Any> = (args?.value(forKey: "item") as? Dictionary<String, Any>) ?? [:]
    // if (item.count == 0) {
    //   print("no item attributes found")
    //   result(false);
    //   return;
    // }

    // let dynamoDb = AWSDynamoDB.default()

    // var itemInput: Dictionary<String, AWSDynamoDBAttributeValue> = [:]
    // for key in item.keys {
    //   guard let value = item[key] else {
    //       print("no value for key")
    //       result(false)
    //       return;
    //   }
    //   let attribute = AWSDynamoDBAttributeValue()
    //   attribute?.s = value as? String ?? ""
    //   itemInput.updateValue(attribute!, forKey: key)
    // }

    // let input = AWSDynamoDBPutItemInput();
    // input?.tableName = tableName;
    // input?.item = itemInput

    // dynamoDb.putItem(input!) { (output, error) in
    //    DispatchQueue.main.async {
    //     if let error = error {
    //       print(error)
    //       result(FlutterError(code: "0", message: error.localizedDescription, details: nil))
    //     } else if let _ = output {
    //       result(true)
    //     } else {
    //       print("No error or output")
    //       result(false)
    //     }
    //   }
    // }
  }

  fun deleteItem(@NonNull call: MethodCall, @NonNull result: Result) {
    // let args = call.arguments as? NSDictionary
          
    // let tableName: String = (args?.value(forKey: "table_name") as? String) ?? ""
    // let item: Dictionary<String, Any> = (args?.value(forKey: "item") as? Dictionary<String, Any>) ?? [:]
    // if (item.count == 0) {
    //   print("no item attributes found")
    //   result(false);
    //   return;
    // }

    // let dynamoDb = AWSDynamoDB.default()

    // var itemInput: Dictionary<String, AWSDynamoDBAttributeValue> = [:]
    // for key in item.keys {
    //   guard let value = item[key] else {
    //       print("no value for key")
    //       result(false)
    //       return;
    //   }
    //   let attribute = AWSDynamoDBAttributeValue()
    //   attribute?.s = value as? String ?? ""
    //   itemInput.updateValue(attribute!, forKey: key)
    // }

    // let input = AWSDynamoDBDeleteItemInput();
    // input?.tableName = tableName;
    // input?.key = itemInput

    // dynamoDb.deleteItem(input!) { (output, error) in
    //    DispatchQueue.main.async {
    //     if let error = error {
    //       print(error)
    //       result(FlutterError(code: "0", message: error.localizedDescription, details: nil))
    //     } else if let _ = output {
    //       result(true)
    //     } else {
    //       print("No error or output")
    //       result(false)
    //     }
    //   }
    // }
  }

  // func parseOutput(items: [[String:AWSDynamoDBAttributeValue]]) -> Array<Dictionary<String, Any>> {
  //   var array = [Dictionary<String, Any>]()
  //   for item in items {
  //     var dict: [String:Any] = [:]
  //     for key in item.keys {
  //       guard let value = item[key] else {
  //           print("no value for key")
  //           return [Dictionary<String, Any>]()
  //       }
  //       dict.updateValue(parseAttribute(value), forKey: key)
  //     }
  //     array.append(dict)
  //   }
    
  //   return array
  // }

  // func parseAttribute(_ attribute: AWSDynamoDBAttributeValue) -> Any {
  //   if let value = attribute.s {
  //     return value
  //   } else if let value = attribute.n, let numValue = Double(value) {
  //     return numValue
  //   } else if let value = attribute.l {
  //     var array = [Any]()
  //     for item in value {
  //       array.append(parseAttribute(item))
  //     }
  //     return array
  //   } else {
  //     return "unknown"
  //   }
  // }
}