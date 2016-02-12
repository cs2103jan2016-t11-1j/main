# Tidy Yard

Tidy Yard is a system for managing TODOs for power users who don't want to leave the command line.

# Getting Started
1. Download the jar
2. Run:
    java TidyYard.jar <Name of TODO File>

# Usage
## Add
### Normal
    a Take out the trash
### With a due date
    a monday Walk the dog
### With a priority
    a #B monday World domination
    a #A tuesday World Peace
## Display
    dis
    > 1. 11/2/2016 #B TODO: World domination
    > 2.              TODO: Take out the trash
    > 3. 11/2/2016    TODO: Walk the dog
### Only Display TODO items
    dis t
    > 1. 11/2/2016 #B TODO: World domination
### Display Organized by Priority
    dis p
    > 1. 11/2/2016 #A TODO: World Peace
    > 1. 11/2/2016 #B TODO: World domination
    > 2.              TODO: Take out the trash
    > 3. 11/2/2016    TODO: Walk the dog
## Change the Status
    mark done 2
    > 1. DONE: Take out the trash
    mark done 3
    > 1. TODO: Take out the trash
## Undo
    mark done 2
    > 1. DONE: Take out the trash
    u
    > 1. TODO: Take out the trash
