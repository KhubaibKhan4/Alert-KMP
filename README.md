# Alert-KMP

[![Maven Central](https://img.shields.io/maven-central/v/io.github.khubaibkhan4/alert-kmp.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.khubaibkhan4/alert-kmp)
![GitHub License](https://img.shields.io/github/license/KhubaibKhan4/Alert-KMP)
![GitHub Issues](https://img.shields.io/github/issues/KhubaibKhan4/Alert-KMP)
![GitHub Pull Requests](https://img.shields.io/github/issues-pr/KhubaibKhan4/Alert-KMP)
![GitHub Last Commit](https://img.shields.io/github/last-commit/KhubaibKhan4/Alert-KMP)
![GitHub Stars](https://img.shields.io/github/stars/KhubaibKhan4/Alert-KMP?style=social)

![Supported Platforms](https://img.shields.io/badge/platform-Android-green.svg)
![Supported Platforms](https://img.shields.io/badge/platform-iOS-blue.svg)
![Supported Platforms](https://img.shields.io/badge/platform-JS-yellow.svg)
![Supported Platforms](https://img.shields.io/badge/platform-WASM-yellow.svg)
![Supported Platforms](https://img.shields.io/badge/platform-JVM-red.svg)
<img src="https://img.shields.io/liberapay/patrons/KhubaibKhanDev.svg?logo=liberapay">


## Overview

Alert-KMP is a Kotlin Multiplatform (KMP) library designed to facilitate native notification displays across Android, iOS, Web, and Desktop platforms using JetBrains Compose Multiplatform. It provides a unified API for triggering notifications that seamlessly integrates into Kotlin's multiplatform ecosystem.

## Features

- **Platform Agnostic:** Supports Android, iOS, Web, and Desktop platforms through Kotlin Multiplatform.
- **Compose Multiplatform Integration:** Seamlessly integrates with JetBrains Compose Multiplatform UI framework.
- **Customizable Notifications:** Easily customize notification content, icons, actions, and behaviors to suit application needs.
- **Event Handling:** Provides callbacks and event listeners for notification actions and dismissal.

## Installation

You can include Alert-KMP in your project by adding the following dependency:

### Compose Multiplatform Setup

**Version Catalog**
```toml
[versions]
alertKmp = "0.0.7"

[libraries]
alert-kmp = { module = "io.github.khubaibkhan4:alert-kmp", version.ref = "alertKmp" }
```

```groovy
implementation("io.github.khubaibkhan4:alert-kmp:0.0.7")
```

### Platform Specific
```
**alert-kmp-jvm**
implementation("io.github.khubaibkhan4:alert-kmp-jvm:0.0.7")

**alert-kmp-android**
implementation("io.github.khubaibkhan4:alert-kmp-android:0.0.7")

**alert-kmp-js**
implementation("io.github.khubaibkhan4:alert-kmp-js:0.0.7")


**alert-kmp-wasm-js**
implementation("io.github.khubaibkhan4:alert-kmp-wasm-js:0.0.7")


```


## Usage
```groovy
import io.github.khubaibkhan4.alert.Notification
import io.github.khubaibkhan4.alert.NotificationType
import io.github.khubaibkhan4.alert.createNotification

fun main() {
    val notification = createNotification(NotificationType.TOAST)
    notification.show("Hello, World!")

OR 

 Notify(message= "Hellow World!", duration=NotificationDuration.SHORT)
}
```
## Notification Types
The library supports multiple notification types:

- **NotificationType.TOAST** - Displays a toast message.
- **NotificationType.ALERT** - Displays an alert dialog.
- **NotificationType.TOP** - Displays a notification at the top of the screen.
- **NotificationType.CUSTOM** - Displays a custom notification (implementations vary by platform).

## 🤝 Connect with Me

Let's chat about potential projects, job opportunities, or any other collaboration! Feel free to connect with me through the following channels:

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/khubaibkhandev)
[![Twitter](https://img.shields.io/badge/Twitter-Follow-blue?style=for-the-badge&logo=twitter)](https://twitter.com/codespacepro)
[![Email](https://img.shields.io/badge/Email-Drop%20a%20Message-red?style=for-the-badge&logo=gmail)](mailto:18.bscs.803@gmail.com)

  ## 💰 You can help me by Donating
  [![BuyMeACoffee](https://img.shields.io/badge/Buy%20Me%20a%20Coffee-ffdd00?style=for-the-badge&logo=buy-me-a-coffee&logoColor=black)](https://buymeacoffee.com/khubaibkhan) [![PayPal](https://img.shields.io/badge/PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://paypal.me/18.bscs) [![Patreon](https://img.shields.io/badge/Patreon-F96854?style=for-the-badge&logo=patreon&logoColor=white)](https://patreon.com/MuhammadKhubaibImtiaz) [![Ko-Fi](https://img.shields.io/badge/Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/muhammadkhubaibimtiaz) 

## Screenshots
| <img src="https://github.com/KhubaibKhan4/Alert-KMP/blob/master/assests/screenshots/1.png" alt="Mobile Screenshot" width="300"> |
 ![Screenshot 2](https://github.com/KhubaibKhan4/Alert-KMP/blob/master/assests/screenshots/2.png) 
 ![Screenshot 3](https://github.com/KhubaibKhan4/Alert-KMP/blob/master/assests/screenshots/3.png) 

 ## Web Toast
 ![Screenshot 2024-07-19 114919](https://github.com/user-attachments/assets/617a3bc6-c129-45fb-acd2-83fb52866ce9)
 ## Web Top Toast
 ![Screenshot 2024-07-19 115339](https://github.com/user-attachments/assets/dd0ba090-21d9-4566-8097-7cc6eaae252d)
 ## Web Custom 
 ![Screenshot 2024-07-19 115753](https://github.com/user-attachments/assets/cfdc1782-8f13-4fff-bda3-dc97214f495f)


## Demo

https://github.com/KhubaibKhan4/Alert-KMP/assets/98816544/f554204b-d484-49a3-b409-caa7e95c5d04



  ## Contribution Guidelines
We welcome contributions to the Alert-KMP Library Project! To contribute, please follow these guidelines:

- **Reporting Bugs**: If you encounter a bug, please open an issue and provide detailed information about the bug, including steps to reproduce it.
- **Suggesting Features**: We encourage you to suggest new features or improvements by opening an issue and describing your idea.
- **Submitting Pull Requests**: If you'd like to contribute code, please fork the repository, create a new branch for your changes, and submit a pull request with a clear description of the changes.

## Code of Conduct
We expect all contributors and users of the Alert-KMP Library Project to adhere to our code of conduct. Please review the [Code of Conduct](CODE_OF_CONDUCT.md) for details on expected behavior and reporting procedures.
