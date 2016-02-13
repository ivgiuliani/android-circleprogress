# android-circleprogress

A simple progress indicator on a circle.

![Screen](https://github.com/kratorius/android-circleprogress/raw/master/demo/images/showcase.png)


## Requirements

* API 9+


## Getting started

Just add the library to your project and add a view like this:

``` xml
<com.github.kratorius.circleprogress.CircleProgressView
    android:id="@+id/crc_standard"
    android:layout_width="150dp"
    android:layout_height="150dp"
    android:layout_gravity="center"
    circleProgress:circleProgressValue="75" />
```

For more examples, check the [demo project](https://github.com/kratorius/android-circleprogress/tree/master/demo).


## Available view properties

* `circleProgressThickness`: controls the thickness of the circle
* `circleProgressColor`: the color of the circle
* `circleProgressValue`: the "progress" (in the 0-100 range)
* `circleProgressStartAngle`: starting angle
* `circleProgressText`: optional text inside the circle
* `circleProgressTextColor`: above text's color
* `circleProgressTextSize`: above text's size
* `circleProgressStartAnimation`: one of `none`, `roll`, `fadeIn`, `incremental`, `thicknessExpand`.

