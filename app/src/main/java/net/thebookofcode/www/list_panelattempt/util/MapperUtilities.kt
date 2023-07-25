package net.thebookofcode.www.list_panelattempt.util

import net.thebookofcode.www.list_panelattempt.room.entity.*
import java.util.*

fun pythonToWord(
    pythonWord: PythonWord
):Word{
    return Word(
        key = pythonWord.key,
        value = pythonWord.value,
        syntax = pythonWord.syntax,
        code = pythonWord.code
    )
}

fun cppToWord(
    cppWord: CppWord
):Word{
    return Word(
        key = cppWord.key,
        value = cppWord.value,
        syntax = cppWord.syntax,
        code = cppWord.code
    )
}

fun javaToWord(
    javaWord: JavaWord
):Word{
    return Word(
        key = javaWord.key,
        value = javaWord.value,
        syntax = javaWord.syntax,
        code = javaWord.code
    )
}

fun wordToBookmark(
    word: Word
):BookmarkWord{
    return BookmarkWord(
        key = word.key,
        value = word.value,
        syntax = word.syntax,
        code = word.code,
        date = Calendar.getInstance().time
    )
}

fun pythonListToWordList(listWords:List<PythonWord>):List<Word>{
    return listWords.map { pythonToWord(it) }
}

fun cppListToWordList(listWords:List<CppWord>):List<Word>{
    return listWords.map { cppToWord(it) }
}

fun javaListToWordList(listWords:List<JavaWord>):List<Word>{
    return listWords.map { javaToWord(it) }
}