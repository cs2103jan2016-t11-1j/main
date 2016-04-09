# Tidy Yard

Tidy Yard is a system for managing TODOs for power users who don't want to leave the command line.

# Getting Started
1. Download the jar
2. Run:
    java TidyYard.jar <Name of TODO File>

# Usage
## Add
### Normal
    add Take out the trash
### With a due date
    add Walk the dog # monday
### With a priority
    add World domination #2 monday
    add World Peace #1 tuesday
//## Display 
    dis
    > 1. 11/2/2016 #B TODO: World domination
    > 2.              TODO: Take out the trash
    > 3. 11/2/2016    TODO: Walk the dog
//### Only Display TODO items
    dis t
    > 1. 11/2/2016 #B TODO: World domination
//### Display Organized by Priority
    sortp
    > 1. 11/2/2016 #A TODO: World Peace
    > 1. 11/2/2016 #B TODO: World domination
    > 2.              TODO: Take out the trash
    > 3. 11/2/2016    TODO: Walk the dog
//## Change the Status
    done 2
    > 1. DONE: Take out the trash
    done 3
    > 1. TODO: Take out the trash
//## Undo
    mark done 2
    > 1. DONE: Take out the trash
    undo
    > 1. TODO: Take out the trash
