## Data Service

A java service designed to handle the data processing required to turn base64 pdfs into a dataset that can easily be consumed by the web frontend.

This task involves:
- Generating images
- Creating reference data (What is the height and width of the document? How many pages does it have?)
- Convert local image coordinates to text
- Convert groups of text to data formats

Note - The /meta endpoint will discard any duplicate pages.