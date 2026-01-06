#include <SoftwareSerial.h>

#define rxPin 2
#define txPin 3


SoftwareSerial mySerial = SoftwareSerial(rxPin, txPin);

String incomingData = "";
bool isDoorClosed = true;

void setup() {
  Serial.begin(9600);
  mySerial.begin(9600);
  pinMode(8, OUTPUT);
  pinMode(6, INPUT_PULLUP);
  pinMode(5, OUTPUT);

  digitalWrite(8, HIGH);
}

void loop() {

  if (mySerial.available() > 0) {
    incomingData = mySerial.readString();
    incomingData.trim();

    if (incomingData == "9876") {
      digitalWrite(8, LOW);  // Lock ON (Open)
      Serial.println("Lock Opened via Phone");
      Serial.print("1");
      isDoorClosed = false;
    } else if (incomingData == "0") {
      digitalWrite(8, HIGH);  // Lock OFF (Close)
      Serial.println("Lock Closed via Phone");
      isDoorClosed = true;
    } else {
      digitalWrite(8, HIGH);
    }
  }

  int sensorVal = digitalRead(6);

  // Auto-lock logic
  if (sensorVal == LOW && isDoorClosed == false) {
    digitalWrite(8, HIGH);
    Serial.print("0");
    isDoorClosed = true;
  }
}



// String incomingData = "";
// bool isDoorClosed = true; 

// void setup() {
//   digitalWrite(8, HIGH);    
//   pinMode(8, OUTPUT);      
  
//   Serial.begin(9600);
  
//   pinMode(6, INPUT_PULLUP);
//   pinMode(5, OUTPUT);     
  
//   delay(200);
// }

// void loop() {
//   if (Serial.available() > 0) {
//     incomingData = Serial.readString();
//     incomingData.trim();                                                                                                                                                                                                                                                                     
                                           
//     if (incomingData == "9876") {
//       digitalWrite(8, LOW); // Lock ON (Open)
//       Serial.println("Lock Opened via Phone");
//       Serial.print("1");                                                                                                                                                     
//       isDoorClosed = false;
      
//       delay(4000); 
//     }
//     else if (incomingData == "0") {
//       digitalWrite(8, HIGH);  // Lock OFF (Close)
//       Serial.println("Lock Closed via Phone");
//       isDoorClosed = true;                       
//     }
//   }
  
//   // int sensorVal = digitalRead(6);

//   // if (sensorVal == LOW && isDoorClosed == false) { 
//   //   delay(500);
//   //   digitalWrite(8, LOW); 
//   //   Serial.print("0"); 
//   //   isDoorClosed = true;
//   // } 
// }