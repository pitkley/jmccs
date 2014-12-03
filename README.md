# jMCCS

This is a Java library for controlling monitors using [MCCS](http://en.wikipedia.org/wiki/Monitor_Control_Command_Set) via [DDC/CI](http://en.wikipedia.org/wiki/Display_Data_Channel#DDC.2FCI).
If the monitor supports DDC/CI, one can use this library to configure (almost) everything one can set via the On-Screen-Display.

This is achieved by using native system-libraries which are either supplied by the operating system itself or included in this library.
Currently, the following operating systems are supported:

- [Windows](windows/src/main/java/de/pitkley/jmccs/monitor/WindowsMonitor.java) using `User32` and `Dxva2` via [`JNA`](https://github.com/twall/jna)
- [OS X](osx/src/main/java/de/pitkley/jmccs/monitor/OSXMonitor.java) using the included `libmccs`-library based on [`DDC-CI-Tools-for-OS-X`](http://github.com/jontaylor/DDC-CI-Tools-for-OS-X) via [`JNA`](https://github.com/twall/jna)

To add support for an operating system, one has to provide an implementation of [`Monitor`](monitor/src/main/java/de/pitkley/jmccs/monitor/Monitor.java) using a *Service Provider Interface*.
