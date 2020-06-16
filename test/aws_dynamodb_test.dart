import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:aws_dynamodb/aws_dynamodb.dart';

void main() {
  const MethodChannel channel = MethodChannel('aws_dynamodb');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });
}
