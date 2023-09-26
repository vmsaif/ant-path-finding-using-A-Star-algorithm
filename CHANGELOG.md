# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added

- Mouse drag support for tile type changes.
- Unique icons for each tile type.
- Counter for each tile type. (Shows the number of tiles of each type)
- Timer 1 - Time took for the user to create mage.
- Timer 2 - Time took for the AI to solve the Mage solution (duration for AI to **graphically** reach the goal).
- Window can now be resized.

### Changed

- "No Path Found" message now centrally aligned on screen.
- Optimized initial rendering speed for the ant image.
- Button placement from right column to top row.
- Adjusted ant's speed consistency across different tile type.
- Remove the word "Location" from the Start and Goal buttons.

### Fixed

- Crash issue when "Search" button is clicked a second time without resetting.

### removed

- Removed the option to disable A* Search.

## [v1.0.2] - 2023-09-22

### Added

- Added cost associated with each type of tile.
- Added Reset button to reset the board to its original state.

### Fixed

- Fixed graphics crash for complex mazes.
- Also increased memory allocation 2GB with -Xmx2G as JVM argument.

### removed

- Removed the option to disable A* Search.

## [v1.0.1] - 2023-09-22

### Added

- Disabled A* Search After clicking the button.

### Fixed

- Fixed graphics crash for complex mazes.

## [v1.0.0] - 2023-09-19

### Added

- Initial release.
