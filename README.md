# Agile Methods for Software Development (SSW-555)

[![Build Status](https://dev.azure.com/robertschaedler/SSW-555/_apis/build/status/robertschaedler3.SSW-555?branchName=master)](https://dev.azure.com/robertschaedler/SSW-555/_build/latest?definitionId=3&branchName=master)

Taught by `Dr. Richard Ens` at Stevens Institute of Technology

## GEDCOM

GEDCOM is a standard format for genealogy data developed by The Church of Jesus Christ of Latter-day Saints. GEDCOM identifies two major entities: individuals and families.  GEDCOM allows you to describe the following characteristics of individuals

- Unique individual ID
- Name
- Sex/Gender
- Birth date
- Death date
- Unique Family ID where the individual is a child
- Unique Family ID where the individual is a spouse

Likewise, GEDCOM allows you to describe the following characteristics of a family:

- Unique family ID 
- Unique individual ID of husband
- Unique individual ID of wife
- Unique individual ID of each child in the family
- Marriage date
- Divorce date, if appropriate

GEDCOM is a line-oriented text file format where each line has three parts separated by blank space: 

1. `level number` (0, 1, or 2) 
1. `tag` (a string of 3 or 4 characters)
1. `arguments` (an optional character string) 

> All lines (or records) begin with a level number that is used to group the information from multiple records. Records with level numbers 1 and 2 are always in the form: 
> 
> `<level_number> <tag> <arguments>`

> Lines with level number 0 have one of two different forms. This first form has the form:
>
> `0 <id> <tag>`
>
> where `<tag>` is `INDI` or `FAM`. The `<id>` field between the `0` and the tag is a unique identifier used to identify an individual or a family. 
> 
> The second versionof level 0 records has the form:
> 
> `0 <tag> <arguments that may be ignored>`
> 
> where `<tag>` is `HEAD`, `TRLR`, or `NOTE`.

## Documentation
- [GEDFile](docs/gedfile.md)
- [Individual](docs/individual.md)
- [Family](docs/family.md)

## Resources
- [Azure Pipelines](https://dev.azure.com/robertschaedler/SSW-555/_build)
- [Maven](https://maven.apache.org/)