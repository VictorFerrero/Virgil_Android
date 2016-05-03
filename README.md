# Virgil
Android application to serve as a personal museum tour guide.

---
> #####Contributors:<br/>
> Amr Hassaballah - hassaballah@wisc.edu <br/>
> Summer Wilken - wilken@wisc.edu <br/>
> Ty Talafous - talafous@wisc.edu <br/>
> Tyler Phelps - Phelps3@wisc.edu <br/>
> Victor Ferrero - vferrero@wisc.edu

---

##Updates:
#####MAR 6-12
 - Project planning and group delegating. Started a Slack to help organize group communication. Set personal milestones for model and API development. - Tyler
 - Fine-tuned xml layouts. Gathered icons and bitmaps. Researched open source code for Exhibit / Gallery parallax layout. - Summer

#####13-19
 - Wrote the model object files and the network API skeleton and interface. - Tyler
 - Started writing XML skeletons : toolbar, exhibit /gallery layout, beacon, map, and favorites view. - Summer
 - Designed table structure for MySQL database. - Victor

#####20-26
 - Wrote the API to make network calls to the back end and parse JSON data into local model objects. Created methods in the API to effectively run network calls asynchronously. Built the local database for storing user's saved favorites. Created a new task delegation sheet to better track team member's current assignments. - Tyler 
 - Started working on app icon. Added more detail to xml layers. Added some menu navigation. - Summer
 - Wrote all server routes for the mobile app and cms version 1. This does not include beacon support yet. - Victor

#####27- APR 2
 - Wrote asynchronous functions to run database processes through the API. Added API support for fetching event data from the backend. Adjusted code to pull and temporarily save content images from the backend to the native storage. - Tyler
 - Tested the entire backend again to ensure it is ready for cms to be built. All routes work as expected, file uplaods are handled properly. - Victor
 - Started design of cms. Helped with NavigationDrawer. Current Layout.-Summer

#####APR 3 - 9

- Pulled images from server asynchronously. Made adjustments and additions to the event object. Patched file naming bugs in the repo. - Tyler
- Added Navigation Drawer to layouts. Changed icon. Changed Gallery content view (added header, image view, and text view) and updated gallery content fragments and adapters with hard-coded values to demonstrate functionality. Did UI/UX cleanup. Current Layout -Summer
 
#####10-16
- Serialized the API, model, and database. Patched database bugs in the ORM and API. Added additional database functionality. Added the current day’s hours to the MuseumSelect tiles. - Tyler
- Added navigation to toolbar menu, added favorites icons to museum gallery/exhibit view (using Tyler Phelps' code), and added events xml with fragment/adapter skeleton java code to museum gallery/exhibit view. - Summer
 
#####17-23
- Displayed thumbnails for museums on museumSelect and favorites view. Created a 2 file caching system for temporarily saving images locally to keep the API serializable and the activity switching efficient. Set try/catch blocks in place to save the app in case of a back end malfunction. - Tyler
- Wrote CMS html, css, and angularjs for client-side validation -- added ng binding to html tags and started populating drop-downs and forms from code-behind with hard-coded values. -Summer
 
#####24-30
- Made the thumbnail image fetching more efficient. Updated the cache to a 3 file system to make it more efficient and lighter on local storage usage. Implemented the museum map feature. Small UI fixes. Updated the API to pull and parse JSON event objects properly. - Tyler
- Added Navigation Drawer navigation, fixed Favorites xml so it scales on different Android devices, added Beacon adapter and fragment java code skeleton. Changed Beacon functionality so beacon button changes color when fetching different content. -Summer

---
> 
> ##Copyright 2016 Virgil Museum App
>
>Licensed under the Apache License, Version 2.0 (the "License"); </br>
>you may not use this file except in compliance with the License. </br>
>You may obtain a copy of the License at                          </br>
>
>    http://www.apache.org/licenses/LICENSE-2.0                   </br>
>
>Unless required by applicable law or agreed to in writing, software </br>
>distributed under the License is distributed on an "AS IS" BASIS,   </br>
>WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. </br>
>See the License for the specific language governing permissions and      </br>
>limitations under the License.                                           </br>
>
---

##Modified Resources
-https://github.com/chrisbanes/cheesesquare by chrisbanes
-http://manishkpr.webheavens.com/android-material-design-tabs-collapsible-example/ by Munish Kapoor


