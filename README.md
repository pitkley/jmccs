# jMCCS

This is a Java library for controlling monitors using [MCCS](http://en.wikipedia.org/wiki/Monitor_Control_Command_Set) via [DDC/CI](http://en.wikipedia.org/wiki/Display_Data_Channel#DDC.2FCI).
If the monitor supports DDC/CI, this library can be used to configure (almost) everything that can be set via the On-Screen-Display.

The library requires operating system-specific implementations, currently the following exist:

* [Windows][jmccs-win]
* [Mac OS X][jmccs-osx]

## Using jMCCS

### Maven dependency

jMCCS is a Maven project and available in Maven Central.
You can use the following snippet to get jMCCS as a dependency.

```xml
<dependency>
  <groupId>de.pitkley.jmccs</groupId>
  <artifactId>jmccs</artifactId>
  <version>0.2.0</version>
</dependency>
```

### Example

Following is a quick example on how you can set the brightness (luminance) of your monitors.

```java
MonitorManager monitorManager = MonitorManager.get();
List<Monitor> monitors = monitorManager.getMonitors();

for (Monitor monitor : monitors) {
    monitor.setVCPFeature(VCPCode.LUMINANCE, 75);
}
```

Additionally, you can use the [helper-class][monhelp] to replace any `setVCPFeature`-calls by more meaningful calls.

```java
MonitorManager monitorManager = MonitorManager.get();
List<MonitorHelper> monitors = monitorManager.getMonitors()
                                   .stream()
                                   .map(MonitorHelper::new)
                                   .collect(Collectors.toList());

for (MonitorHelper monitor : monitors) {
    monitor.setLuminance(75);
}
```

## Creating an implementation

To add support for an operating system, you have to provide an implementation for the abstract class [MonitorManager][monman] and the interface [Monitor][mon].
The `MonitorManager` has to be registered as a *Service Provider* by supplying a text-file in the `META-INF`-directory.
See [this file][spi-ex] as an example.
(The only dependency you will need is the one above.)

## License

This project is licensed under MIT.

[jmccs-win]: https://github.com/pitkley/jmccs-win
[jmccs-osx]: https://github.com/pitkley/jmccs-osx
[monman]: monitor/src/main/java/de/pitkley/jmccs/monitor/MonitorManager.java
[mon]: monitor/src/main/java/de/pitkley/jmccs/monitor/Monitor.java
[monhelp]: helper/src/main/java/de/pitkley/jmccs/monitor/MonitorHelper.java
[spi-ex]: https://github.com/pitkley/jmccs-osx/blob/master/src/main/resources/META-INF/services/de.pitkley.jmccs.monitor.MonitorManager

