##Backseat Driver
Backseat Driver is an Android application created at [MHacks IV](http://mhacks.org), designed to work with the [Open-XC platform](http://openxcplatform.com). It presents a simple visual and auditory interface to help teach new drivers how to operate a manual transmission. In its current form, the application only works with vehicles using Chrysler's PowerNET electrical architecture (see notes about Open-XC and Chrysler below).

This project won the "Connected Car Award" by [Chrysler](http://chrysler.com) and the "Best Use of Mobile Sensor Data Award" by [FarmLogs](http://farmlogs.com) at MHacks IV.

[Check out the app in motion!](https://www.youtube.com/watch?feature=player_embedded&v=76lRe-zwPvA)

####Notes about Open-XC and Chrysler

This application was designed using a CrossChasm C5 device running a slightly modified firmware to work with Chrysler's PowerNET electrical architecture. We cannot promise that this will work for other vehicles - in fact, if this project is paired with a base Open-XC Android library, likely certain key data (at the least, the sensor data on ClutchPedalStatus) will be lost.

####Acknowledgements
The core MHacks team was Colin Szechy, Michael Ray, Katelyn Dunaskirk, and Riyu Banerjee.

Special thanks to Chrysler for sponsoring MHacks IV, as well as providing the hardware, API, and cash prizes! And another special thanks to [Michigan Hackers](http://michiganhackers.org) and [MPowered Entrepreneurship](http://mpowered.umich.edu) for putting on yet another fantastic MHacks!