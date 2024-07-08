<h1 style="text-align:center;">Diore</h1>

<p style="text-align:center;">
Diamond-based economy plugin with Vault.
</p>

[![Spiget Raiting](https://img.shields.io/spiget/stars/117800?style=for-the-badge)](https://www.spigotmc.org/resources/diore.117800/)

## About

Diore is a standalone economy plugin for Spigot, using diamond ore as currency, allows players to create accounts, deposit, transfer, and withdraw diamond ore, with Vault Hook integration for enhanced compatibility and security.

## API

### Maven

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
    <groupId>com.github.Ximronno</groupId>
    <artifactId>Diore</artifactId>
    <version>RELEASE-VERSION</version>
</dependency>
```
### Gradle

**Groovy DSL:**
```gradle
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
        implementation 'com.github.Ximronno:Diore:RELEASE-VERSION'
}
```


## Contributing
Contributions are welcome, just open a pull request.
