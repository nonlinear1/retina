# ch.ethz.idsc.gokart <a href="https://travis-ci.org/idsc-frazzoli/retina"><img src="https://travis-ci.org/idsc-frazzoli/retina.svg?branch=master" alt="Build Status"></a>

Software to operate the go-kart in autonomous and manual modes.
The performance of the go-kart hardware and software are documented in [reports](doc/reports.md).

> The code in the repository operates a heavy and fast robot that may endanger living creatures. We follow best practices and coding standards to protect from avoidable errors - see [development guidelines](doc/development_guidelines.md).

## Gallery Autonomous Driving

<table>
<tr>
<td>

![usecase_gokart](https://user-images.githubusercontent.com/4012178/35968269-a92a3b46-0cc3-11e8-8d5e-1276762cdc36.png)

[Trajectory pursuit](https://www.youtube.com/watch?v=XgmS8CP6gqw)

<td>

![planning_obstacles](https://user-images.githubusercontent.com/4012178/40268689-2af06cd4-5b72-11e8-95cf-d94edfdc3dd1.png)

Navigation
[initial](https://www.youtube.com/watch?v=xLZeKFeAokM),
[demo](https://www.youtube.com/watch?v=UnqaZavf3G0)

<td>

![autonomous_braking](https://user-images.githubusercontent.com/4012178/46241930-4051b080-c3c1-11e8-84b5-909d698d4bdf.png)

[Autonomous braking](https://www.youtube.com/watch?v=b_Sqy2TmKIk)

<td>

![visioneventbased](https://user-images.githubusercontent.com/4012178/45996325-21d77680-c09c-11e8-9d0a-ffdd4dfba62b.png)

[Event-based SLAM](https://www.youtube.com/watch?v=NKylhRHbnGA), [Fig. 8](https://www.youtube.com/watch?v=NpCwG_32Cr8)

</tr>

<tr>
<td>

![gokart_mpc](https://user-images.githubusercontent.com/4012178/52276469-9d063e80-2952-11e9-8ce8-9f652238a0d8.png)

Kin. MPC [outside](https://www.youtube.com/watch?v=B0QmMS1Dp8E), [inside](https://www.youtube.com/watch?v=N_GqjWpRkR4)

<td>

![dynamic_mpc](https://user-images.githubusercontent.com/4012178/54411184-c4d58880-46ee-11e9-8703-cff85af1bc55.png)

Dyn. MPC [outside](https://www.youtube.com/watch?v=59E95E_RkH8), [inside](https://www.youtube.com/watch?v=t42Yd-1C9iM)

<td>

![mpc_ann](https://user-images.githubusercontent.com/4012178/67439187-da505080-f5f5-11e9-8d55-c1abb5afe41c.jpg)

[ANN MPC](https://www.youtube.com/watch?v=YwqjDYcEqpM)

</tr>
</table>

## Student Projects

The student projects are supervised by Andrea Censi, Jacopo Tani, Alessandro Zanardi, and Jan Hakenberg.
The gokart is operated at Innovation Park Dübendorf since December 2017.

### 2017

* Noah Isaak, Richard von Moos (BT): micro autobox programming, low-level actuator logic

### 2018

* Mario Gini (MT): simultaneous localization and mapping for event-based vision systems inspired by Weikersdorfer/Hoffmann/Conradt; reliable waypoint extraction and following
* Yannik Nager (MT): bayesian occupancy grid; trajectory planning
* Valentina Cavinato (SP): tracking of moving obstacles
* Marc Heim (MT): calibration of steering, motors, braking; torque vectoring; track reconnaissance; [model predictive contouring control](https://www.youtube.com/watch?v=7dnlF5QVJoo); synthesis of engine sound; [drone video](https://www.youtube.com/watch?v=bmIQxtiAzhQ)

### 2019

* Michael von Büren (MT): simulation of gokart dynamics, neural network as model for MPC
* Joel Gächter (MT): sight-lines mapping, clothoid pursuit, planning with clothoids
* Antonia Mosberger (BT): power steering, anti-lock braking, lane keeping
* Maximilien Picquet (SP): Pacejka parameter estimation using an unscented Kalman filter
* Thomas Andrews (SP): Torque Controlled Steering extenstion to MPC.


## Gallery Rendering

<table>
<tr>
<td>

![velocity](https://user-images.githubusercontent.com/4012178/64156621-70a59880-ce35-11e9-8981-c33aa2149934.png)

[Doughnuts](https://www.youtube.com/watch?v=DRjB3XNt4Vo)

<td>

![clothoid_pursuit](https://user-images.githubusercontent.com/4012178/64125542-9a65ad80-cdaa-11e9-9140-babb094e49b3.png)

[Clothoid pursuit](https://www.youtube.com/watch?v=nF43WfSGdls)

<td>

![lane_keeping](https://user-images.githubusercontent.com/4012178/64124789-478af680-cda8-11e9-8d7a-55e739436138.png)

[Lane keeping](https://www.youtube.com/watch?v=8L-lCSk10ig)

<td>

![mpc_lidar](https://user-images.githubusercontent.com/4012178/64124647-d0edf900-cda7-11e9-90ee-8363865adfa7.png)

[MPC](https://www.youtube.com/watch?v=TZH3MWjp6PE)

</tr>
<tr>
<td>

![clothoid_rrts](https://user-images.githubusercontent.com/4012178/65374679-e2327300-dc8c-11e9-9a9e-d26d673d8176.png)

[Clothoid RRT*](https://www.youtube.com/watch?v=xbSG7XggDE0)

</tr>
</table>

## Gallery Manual Driving

<table>
<tr>
<td>

![torquevectoring](https://user-images.githubusercontent.com/4012178/49995554-c75c0100-ff8c-11e8-8a86-f50b6e6833ad.jpg)

[Torque Vectoring](https://www.youtube.com/watch?v=szKhTCxhPyI)

<td>

![doughnuts](https://user-images.githubusercontent.com/4012178/60753913-c5fe2e80-9fd9-11e9-9aa9-af307331b246.jpg)

[Doughnuts](https://www.youtube.com/watch?v=zcBImlS0sE4)

</tr>
</table>

## Press Coverage

* [2018-06 bitluni's lab](https://www.youtube.com/watch?v=GQVsl4fV3O0)
* [2018-10 Innovationspark](https://www.switzerland-innovation.com/zurich/node/414)
* [2018-12 Telezueri](https://www.telezueri.ch/zuerinews/200-millionen-franken-fuer-innovationspark-duebendorf-133778855)
* [2019-08 SuperQuark](https://www.raiplay.it/video/2019/08/SuperQuark-73b2ae02-700d-46fb-9fc1-3bdd5a03d5cd.html)

## Architecture

* [`tensor`](https://github.com/idsc-frazzoli/tensor) for linear algebra with physical units
* [`owl`](https://github.com/idsc-frazzoli/retina) for motion planning
* [`lcm`](https://github.com/idsc-frazzoli/lcm-java) *Lightweight Communications and Marshalling* for message interchange, logging, and playback. All messages are encoded using a single type `BinaryBlob`. The byte order of the binary data is `little-endian` since the encoding is native on most architectures.
* [`io.humble`](http://www.humble.io/) for video generation
* [`jSerialComm`](http://fazecast.github.io/jSerialComm/) platform-independent serial port access
* [`ELKI`](https://elki-project.github.io/) for DBSCAN
* [`lwjgl`](https://www.lwjgl.org/) for joystick readout

---

![ethz300](https://user-images.githubusercontent.com/4012178/45925071-bf9d3b00-bf0e-11e8-9d92-e30650fd6bf6.png)

# ch.ethz.idsc.retina <a href="https://travis-ci.org/idsc-frazzoli/retina"><img src="https://travis-ci.org/idsc-frazzoli/retina.svg?branch=master" alt="Build Status"></a>

Sensor interfaces

![retina](https://user-images.githubusercontent.com/4012178/63222215-03db9e80-c1a5-11e9-9d62-30af57c107fd.png)

## Features

* interfaces to lidars Velodyne VLP-16, HDL-32E, Quanergy Mark8, HOKUYO URG-04LX-UG01
* interfaces to inertial measurement unit Variense VMU931
* interfaces to event based camera Davis240C with lossless compression by 4x
* interfaces to LabJack U3

## LIDAR

### Velodyne VLP-16

* point cloud visualization and localization with lidar [video](https://www.youtube.com/watch?v=pykecjwixgg)

### Velodyne HDL-32E

* 3D-point cloud visualization: see [video](https://www.youtube.com/watch?v=abOYEIdBgRs)

distance as 360[deg] panorama

![velodyne distances](https://user-images.githubusercontent.com/4012178/29020149-581e9236-7b61-11e7-81eb-0fc4577b687d.gif)

intensity as 360[deg] panorama

![intensity](https://user-images.githubusercontent.com/4012178/29026760-c29ebbce-7b7d-11e7-9854-9280594cb462.gif)

### Quanergy Mark8

* 3D-point cloud visualization: see [video](https://www.youtube.com/watch?v=DjvEijz14co)

### HOKUYO URG-04LX-UG01

![urg04lx](https://user-images.githubusercontent.com/4012178/29029959-c052da4c-7b89-11e7-8b01-1b4efc3593c0.gif)

our code builds upon the
[urg_library-1.2.0](https://sourceforge.net/projects/urgnetwork/files/urg_library/)

## Inertial Measurement Unit

### VMU931

## Event Based Camera

### IniLabs DAVIS240C

* [SAE with different temporal windows](https://www.youtube.com/watch?v=NKw27ekIosI)
* [SAE with different bucket size](https://www.youtube.com/watch?v=vuXMG3TnZlM)

Rolling shutter mode

<table>
<tr>
<td>

![05tram](https://user-images.githubusercontent.com/4012178/30553969-2948547a-9ca3-11e7-91e8-159806c7e329.gif)

<td>

![04peds](https://user-images.githubusercontent.com/4012178/30553578-f3429ce2-9ca1-11e7-8870-85078c8aa96c.gif)

<td>

![00scene](https://user-images.githubusercontent.com/4012178/30553889-e59c0a5a-9ca2-11e7-8cc3-08de77e21e5e.gif)

</tr>
</table>

Global shutter mode

<table>
<tr>
<td>

![dvs_2500](https://user-images.githubusercontent.com/4012178/34606522-075a20ec-f210-11e7-966a-49384b048809.gif)

2.5[ms]

<td>

![dvs_5000](https://user-images.githubusercontent.com/4012178/34606520-073c7d08-f210-11e7-8ee2-1a35173bbade.gif)

5[ms]

</tr>
</table>

Events only

<table>
<tr>
<td>

![dvs_noaps_1000](https://user-images.githubusercontent.com/4012178/34684372-2eb4b200-f4a5-11e7-891e-74c2123a3bfe.gif)

1[ms]

<td>

![dvs_noaps_2500](https://user-images.githubusercontent.com/4012178/34684373-2eca8ee0-f4a5-11e7-9f70-f41d4722edf7.gif)

2.5[ms]

<td>

![dvs_noaps_5000](https://user-images.githubusercontent.com/4012178/34684374-2ee3aaba-f4a5-11e7-9ac6-72b7ac502793.gif)

5[ms]

</tr>
</table>

AEDAT 2.0, and AEDAT 3.1

* parsing and visualization
* conversion to text+png format as used by the Robotics and Perception Group at UZH
* loss-less compression of DVS events by the factor of 2
* compression of raw APS data by factor 8 (where the ADC values are reduced from 10 bit to 8 bit)

### Device Settings

Quote from Luca/iniLabs:
* *Two parameters that are intended to control framerate:* `APS.Exposure` and `APS.FrameDelay`
* `APS.RowSettle` *is used to tell the ADC how many cycles to delay before reading a pixel value, and due to the ADC we're using, it takes at least three cycles for the value of the current pixel to be output by the ADC, so an absolute minimum value there is 3. Better 5-8, to allow the value to settle. Indeed changing this affects the framerate, as it directly changes how much time you spend reading a pixel, but anything lower than 3 gets you the wrong pixel, and usually under 5-6 gives you degraded image quality.*

We observed that in *global shutter mode*, during signal image capture the stream of events is suppressed. Whereas, in *rolling shutter mode* the events are more evenly distributed.

### streaming DAT files

![hdr](https://user-images.githubusercontent.com/4012178/27771907-a3bbcef4-5f58-11e7-8b0e-3dfb0cb0ecaf.gif)

### streaming DAVIS recordings

![shapes_6dof](https://user-images.githubusercontent.com/4012178/27771912-cb58ebb8-5f58-11e7-9566-79f3fbc5d9ba.gif)

### generating DVS from video sequence

![cat_final](https://user-images.githubusercontent.com/4012178/27771885-0eadb2aa-5f58-11e7-9f4d-78a57e610f56.gif)

### synthetic signal generation 

<table><tr>
<td>

![synth2](https://user-images.githubusercontent.com/4012178/27772611-32cc2e92-5f66-11e7-9d1f-ff15c42d54be.gif)

<td>

![synth1](https://user-images.githubusercontent.com/4012178/27772610-32af593e-5f66-11e7-8c29-64611f6ca3e6.gif)

</tr></table>

## References

* [*Simultaneous localization and mapping for event-based vision systems*](https://mediatum.ub.tum.de/doc/1191908/1191908.pdf) by David Weikersdorfer, Raoul Hoffmann, and Joerg Conradt
* [*Asynchronous event-based multikernel algorithm for high-speed visual features tracking*](https://ieeexplore.ieee.org/document/6899691) by Xavier Lagorce et al.

---

![ethz300](https://user-images.githubusercontent.com/4012178/45925071-bf9d3b00-bf0e-11e8-9d92-e30650fd6bf6.png)
