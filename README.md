# JavaFX Government Tax System (JFXGTDS)

## Description

This project implements a JavaFX-based system for the government tax department to manage and validate tax transaction
files. It allows importing, validating, updating, and deleting records based on checksum validation. Additionally, it
calculates profits and tax for each transaction.

## Features

- Import Transaction File: Import tax transaction files in various formats (CSV, TSV, JSON, XML).

- Validate Records: Validate checksum and detect errors in transactions.

- Update Invalid Records: Edit and recalculate checksum for invalid records.

- Profit Calculation: Calculate profit for each transaction based on prices and quantities.

- Tax Calculation: Calculate the final tax based on the tax rate input by the manager.

## Example Interaction

Import Transaction File: Select the file to import.

View Imported Transactions: View item codes, prices, quantities, and checksums.

Validate Records: Check if the checksum matches and identify any invalid records.

Update Invalid Records: Edit invalid records and update their checksums.

Calculate Tax: Input the tax rate and calculate the final tax based on the records.