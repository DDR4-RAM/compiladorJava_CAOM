package src;

public enum TipoToken {
    // Crear un tipoToken por palabra reservada
    // Crear un tipoToken: identificador, una cadena y numero
    // Crear un tipoToken por cada "Signo del lenguaje" (ver clase Scanner)

    //Formas de Programa
    ID, NUMERO, SEPARADOR_CLASE, SEPARADOR_FUNCION, CADENA, COMENTARIO,

    ASIGNAR,
    //OPERADORES
    MAS, MENOS, ENTRE, POR,
    IGUAL,
    DIFERENTE, MAYOR,
    MENOR, MAYOR_IGUAL,
    MENOR_IGUAL,


    // Palabras clave:
    SI, Y, CLASE, O, VALOR, VAR, FUN, IMP, CICLO, VER, FAL, NULO, RET, ESTE, SUPER,
    ADEMAS, PARA,

    // Operaciones

    //PALABRAS RESERVADAS
    TRUE,
    FALSE, NULL,
    THIS, NUMBER,
    AND,
    OR, WHILE,
    ELSE, FOR,
    RETURN, PRINT,
    IF,

    //SIMBOLOS
    PAR_ABRE, PAR_CIERRE,
    LLAV_ABRE, LLAV_CIERRE,
    COMA, PUNTO, PUNTO_COMA,
    NOT,
    //OTROS
    ERROR,


    // Final de cadena
    EOF
}
