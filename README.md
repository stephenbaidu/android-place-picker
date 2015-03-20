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
        compile 'com.github.stephenbaidu:placepicker:1.0'
    
    }

## Usage
### Create intent
    // Create an intent with `PlacePicker.class`
    Intent intent = new Intent(MainActivity.this, PlacePicker.class);
    
    // Pass your server api key (required)
    intent.putExtra(PlacePicker.PARAM_API_KEY, api_key);
    
    // Pass extra query as one string like below
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

    Copyright 2013-2014 Stephen Baidu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


---