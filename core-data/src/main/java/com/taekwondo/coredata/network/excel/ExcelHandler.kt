package com.taekwondo.coredata.network.excel

import android.content.Context
import android.net.Uri
import com.taekwondo.corecommon.ext.debug
import com.taekwondo.coredata.network.entity.FighterEntity
import com.taekwondo.coredata.network.enums.Gender
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


class ExcelReader @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun readExcelFile(uri: Uri): List<FighterEntity> {
        val entities: MutableList<FighterEntity> = ArrayList()
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream.use {
                val _workbook = XSSFWorkbook(it)
                val sheet: Sheet =
                    _workbook.getSheetAt(0)
                for (row in sheet) {
                    if (row.rowNum == 0) {
                        continue  // Skip header row
                    }
                    val name = row.getCell(0).stringCellValue
                    val age = row.getCell(1).numericCellValue
                    val gender = row.getCell(2).stringCellValue
                    val weight = row.getCell(3).numericCellValue
                    val weightCategory = row.getCell(4).stringCellValue
                    val club = row.getCell(5).stringCellValue
                    val trainer = row.getCell(6).stringCellValue
                    val height = row.getCell(7).numericCellValue
                    FighterEntity(
                        name = name,
                        age = age.toFloat(),
                        gender = when (gender) {
                            "М" -> Gender.MALE
                            "Ж" -> Gender.FEMALE
                            else -> Gender.MALE
                        },
                        weight = weight.toFloat(),
                        weightCategory = weightCategory,
                        club = club,
                        trainer = trainer,
                        height = height.toFloat(),
                    ).apply {
                        entities.add(this)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return entities
    }
}
