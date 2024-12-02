#Project Name: Deadline
##Project Description:
*Deadline* is designed to be a basic task manager, capable of creating, editing, organizing and deleting a variety of user defined tasks. These tasks are displayed on a series of predefined lists. Adding and editing tasks is performed via an Add/Edit wizard. Edit task enables users to change almost every aspect of an existing task, from its title and due date, to its associated tags, list location, notification settings and more. Tasks can also be marked as complete or permanently deleted by button selection. Notifications associated with tasks are designed to be scheduled based on the notifications settings selected, and the tasks priority. App navigation is performed through a mix of taps for button and task selections, and swiping. In the case of the Add/Edit wizard, both navigation options are present. Text fields and dropdown menus in the wizard are also operated via standard tap selection.

##Installation:
Standard procedure for activating via Android Studio. Unzip file in Android Studio Project folder and select *DeadlineSketch* from the app folder.

##Operation:
*Buttons* - Single tap operation
*Lists* - Swipe left and right to navigate. Lists are currently in sidescrolling slideshow format, not a continuous carousel.
*Add/Edit wizard* - Swipe left/right or via navigation buttons at the bottom of the screen. Similar to Lists, the wizard is also in side-scrolling slideshow format.
*Blank fields - Add/Edit wizard* - All bordered fields in the wizard are either text fields or dropdown menus. Attempts to integrate a dropdown arrow into dropdown menus has so far proven unsuccessful.

##Issues and the Future of Deadline:
###Known Issues (incomplete list):
Notifications - scheduled but currently not displayed in the main screen. Cause currently unknown.
Screen Refresh - currently obtained via onResume function calls. The apps current design blocks required communication to allow proper refresh function calls.
Threading - Known lag issues when leaving Home screen for the first time, and on occasion after. Only the first lag is reproducible.
Threading and the Database - currently reliant on sleep calls since no effective solutions were found to force threads to wait for operation completions.
Calendar - current implementation has run into persistent crash issues and is on hold at the moment

###Under Development:
Settings menu - implemented but incomplete
Calendar - addition is planned as soon as crash issue is resolved
OnReboot rescheduling - notification rescheduling for device reboots is currently incomplete. On hold until current notification issues are resolved

###Continued Work:
Resolve issues with Notifications, Calendar and Threading
Implementation of list additions and further protections for safe task management updates
Addition of a list manager screen to allow users to better know what lists they have to work with and/or manage those lists directly

**Repo for CS2063 Mobile App Project (Team 9)**
