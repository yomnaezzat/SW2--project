# FINAL FIX FOR POM FILES

The Maven build is failing because all POM files contain an invalid XML tag `<n>` which Maven doesn't recognize.

## The Issue

In standard Maven POM files, the project name should be specified using the `<name>` tag, not `<n>`.

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
2. Find the line containing the `<n>` tag
3. Replace the **entire tag** (both opening and closing tag) with the correct tag
4. For example, change `<n>common-library</n>` to `<name>common-library</name>`
5. Save the file

## Example Replacements

In common-library/pom.xml, change:
```xml
<artifactId>common-library</artifactId>
<version>1.0-SNAPSHOT</version>
<n>common-library</n>
<description>Common Library for File Repository Services</description>
```

To:
```xml
<artifactId>common-library</artifactId>
<version>1.0-SNAPSHOT</version>
<name>common-library</name>
<description>Common Library for File Repository Services</description>
```

Make similar changes in all the other POM files.

After fixing all files, run Maven again:
```
mvn clean install -DskipTests
```

This should resolve the Maven build errors.
