# TranslationFormatter
removes raw(on-ASCII) text, and adds footnotes. currently designed for WordPress.

## Main class: MainFrame.java
the classes in the package are a stalled, very incomplete attempt at inproving functionality. You probably should just ignore it.

## How to use

Paste any text into the textbox on the left, and the program will remove all lines that contain no alphanumeric characters.
Output will show up on the right.

## Additional features:
- Any lines that start with // will be commented out.
- Also creates footnotes. &lt;h&gt;Any text enclosed in this fake html tag will turn into a footnote at the bottom.&lt;/h&gt;
- &lt;q&gt;This fake html tag acts as an invisible note with text that will be removed&lt;/q&gt;

## Disclaimers

I haven't touched this code in about a year. Might have forgotten to comment/uncomment random stuff.
This isn't going to be maintained much.
