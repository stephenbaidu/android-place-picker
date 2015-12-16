# Android PlacePicker
A library for selecting places via Google Places API. The UI adapts from Google Maps' location search.

## Setup

PlacePicker Library is pushed to jCenter, so make sure you have jcenter in your main gradle file
    
    // Top-level build file where you can add configuration options common to all sub-projects/modules.
    
    buildscript {
        repositories {
            ...
            jcenter()
        }
        dependencies {
            classpath 'com.android.tools.build:gradle:1.1.2'
    
            // NOTE: Do not place your application dependencies here; they belong
            // in the individual module build.gradle files
        }
    }
    
    allprojects {
        repositories {
            ...
            jcenter()
        }
    }


Then you just need to add the following dependency to your `build.gradle`.

    dependencies {
        
        ...
        
        // placepicker
        compile 'com.github.stephenbaidu:placepicker:1.1'
    
    }

## Usage
### Create intent
    // Create an intent with `PlacePicker.class`
    Intent intent = new Intent(MainActivity.this, PlacePicker.class);
    
    // Set your server api key (required)
    intent.putExtra(PlacePicker.PARAM_API_KEY, api_key);
    
    // Set extra query in a one line like below
    intent.putExtra(PlacePicker.PARAM_EXTRA_QUERY, "&components=country:gh&types=(cities)");
    
    // Then start the intent for result
    startActivityForResult(intent, PlacePicker.REQUEST_CODE_PLACE);
    
 
### Use the selected place
 ```java
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PlacePicker.REQUEST_CODE_PLACE && resultCode == RESULT_OK) {
            PlaceDetail placeDetail = PlacePicker.fromIntent(data);
            textPlaceId.setText(placeDetail.placeId);
            textName.setText(placeDetail.description);
            Log.v("=====PlacePicker=====", data.getStringExtra(PlacePicker.PARAM_PLACE_ID));
            Log.v("=====PlacePicker=====", data.getStringExtra(PlacePicker.PARAM_PLACE_DESCRIPTION));
            Log.v("=====PlacePicker=====", data.toString());
            Log.v("=====PlacePicker=====", placeDetail.toString());
        }
    }
```

License
-------

    The MIT License (MIT)

    Copyright (c) 2015 Stephen Baidu

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
