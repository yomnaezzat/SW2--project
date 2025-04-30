# Manual Fix for POM XML Files

The Maven build is failing because all POM files have an unknown XML tag `<n>`.

## Problem Identified

In your Maven build output, you're seeing errors like:
```
Malformed POM C:\Users\yumenna ezzat\Downloads\SW2--project\common-library\pom.xml: Unrecognised tag: 'n' (position: START_TAG seen ...</version>\n    <n>... @15:8)
```

This is happening because Maven doesn't recognize the `<n>` tag. In standard Maven POM files, the project name should be specified using the `<n>` tag.

## Manual Fix Instructions

For each of these files:
- `C:\Users\yumenna ezzat\Downloads\SW2--project\pom.xml`
- `C:\Users\yumenna ezzat\Downloads\SW2--project\common-library\pom.xml`
- `C:\Users\yumenna ezzat\Downloads\SW2--project\api-gateway\pom.xml`
- `C:\Users\yumenna ezzat\Downloads\SW2--project\user-service\pom.xml`
- `C:\Users\yumenna ezzat\Downloads\SW2--project\file-service\pom.xml`
- `C:\Users\yumenna ezzat\Downloads\SW2--project\repository-service\pom.xml`

Do the following:

1. Open the file in a text editor (like Notepad++ or VS Code)
2. Find the line that looks like `<n>project-name</n>` (the project name will vary)
3. Replace this line with `<n>project-name</n>` (change only the tag name from `n` to `n`)
4. Save the file

## Example

Change:
```xml
<version>1.0-SNAPSHOT</version>
<n>common-library</n>
<description>Common Library for File Repository Services</description>
```

To:
```xml
<version>1.0-SNAPSHOT</version>
<n>common-library</n>
<description>Common Library for File Repository Services</description>
```

After fixing all files, run Maven again:
```
mvn clean install -DskipTests
```

This should resolve the Maven build errors.
