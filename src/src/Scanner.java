package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// AUTOR: Carlos Andres Ortiz Montero
public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private int linea = 1;

    private static final Map<String, TipoToken> palabrasReservadas;


    // PUREBA
    // clase hola.fun myfun [ dsandjlksandnlknakl ]imp dna. var variable = value. nulo fal ver  ciclo condicion [ sentencias ] si( cond1 y cond2 o cond3 y cond4) este este  super super
    static {
        palabrasReservadas = new HashMap<>();
        // Clase
        palabrasReservadas.put("clase", TipoToken.CLASE);
        palabrasReservadas.put("este", TipoToken.ESTE);
        palabrasReservadas.put("super", TipoToken.SUPER);
        // Condiciones
        palabrasReservadas.put("si", TipoToken.SI);
        palabrasReservadas.put("y", TipoToken.Y);
        palabrasReservadas.put("o", TipoToken.O);
        // Variables
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("fal", TipoToken.FAL);
        palabrasReservadas.put("ver", TipoToken.VER);
        palabrasReservadas.put("nulo", TipoToken.NULO);
        //Funciones
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("imp", TipoToken.IMP);
        palabrasReservadas.put("ret", TipoToken.RET);
        // Ciclos
        palabrasReservadas.put("ciclo", TipoToken.CICLO); // ES EL MIENTRAS

        /*
        palabrasReservadas.put("ademas", );
        palabrasReservadas.put("para", );


        */
    }

    Scanner(String source) {
        this.source = source;
    }


    //int estado = 0;

    /*public List<Token> getLista() {
        return tokens;
    }*/

    List<Token> scanTokens() {
        //Aquí va el corazón del scanner.

        /*
        Analizar el texto de entrada para extraer todos los tokens
        y al final agregar el token de fin de archivo
         */
        int index = 0;
        String token = "";
        String value = "";
        int estado = 0;
        TipoToken tipoToken = null;
        while (true) {
            try {
                String currentCharacter = String.valueOf(source.charAt(index));
                switch (estado) {
                    case 0:
                        value = "";
                        token = "";
                        if (source.charAt(index) == 'c') {
                            estado = 1;
                            tipoToken = TipoToken.CLASE;
                        } else if (source.charAt(index) == 'v') {
                            estado = 7;
                            tipoToken = TipoToken.VAR;
                        } else if (source.charAt(index) == 'f') {
                            estado = 11;
                            tipoToken = TipoToken.FUN;
                        } else if (source.charAt(index) == 'i') {
                            estado = 15;
                            tipoToken = TipoToken.IMP;
                        } else if (source.charAt(index) == 'n') {
                            tipoToken = TipoToken.NULO;
                            estado = 23;
                        } else if (source.charAt(index) == 's') {
                            estado = 26;
                            tipoToken = TipoToken.SI;
                        } else if (source.charAt(index) == 'r') {
                            estado = 30;
                            tipoToken = TipoToken.RET;
                        } else if (source.charAt(index) == 'e') {
                            estado = 32;
                            tipoToken = TipoToken.ESTE;
                        }
                        break;
                    case 1:
                        if (source.charAt(index) == 'l') {
                            estado = 2;
                        } else if (source.charAt(index) == 'i') {
                            estado = 18;
                            tipoToken = TipoToken.CICLO;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 2:
                        if (source.charAt(index) == 'a') {
                            estado = 3;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 3:
                        if (source.charAt(index) == 's') {
                            estado = 4;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 4:
                        if (source.charAt(index) == 'e') {
                            estado = 17;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 17:
                        if (source.charAt(index) == ' ') {
                            estado = 5;
                        } else {
                            estado = 0;
                        }
                    case 5:
                        if (source.charAt(index) != ' ') {
                            if (tipoToken.equals(TipoToken.VAR)) {
                                estado = 9;
                            } else {
                                estado = 6;
                            }
                            token += currentCharacter;
                        }
                        break;
                    case 6:
                        if (source.charAt(index) == '.') {
                            tokens.add(new Token(tipoToken, "--", token, linea));
                            estado = 0;
                        } else {
                            token += currentCharacter;
                        }
                        break;
                    case 7:
                        if (source.charAt(index) == 'a') {
                            estado = 8;
                        } else if (source.charAt(index) == 'e') {
                            estado = 22;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 8:
                        if (source.charAt(index) == 'r') {
                            estado = 17;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 9:
                        if (source.charAt(index) == '=') {
                            estado = 10;
                        } else {
                            token += currentCharacter;
                        }
                        break;
                    case 10:
                        if (source.charAt(index) == '.') {
                            estado = 0;
                            tokens.add(new Token(tipoToken, value, token, linea));
                        } else {
                            value += source.charAt(index);
                        }

                        break;
                    case 11:
                        if (source.charAt(index) == 'u') {
                            estado = 12;
                        } else if (source.charAt(index) == 'a') {
                            estado = 21;
                            tipoToken = TipoToken.FAL;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 12:
                        if (source.charAt(index) == 'n') {
                            estado = 13;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 13:
                        if (source.charAt(index) == '[') {
                            estado = 14;
                        } else {
                            token += currentCharacter;
                        }
                        break;
                    case 14:
                        if (source.charAt(index) == ']') {
                            tokens.add(new Token(tipoToken, value, token, linea));
                            estado = 0;
                        } else {
                            value += source.charAt(index);
                        }
                        break;
                    case 15:
                        if (source.charAt(index) == 'm') {
                            estado = 16;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 16:
                        if (source.charAt(index) == 'p') {
                            estado = 17;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 18:
                        if (source.charAt(index) == 'c') {
                            estado = 19;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 19:
                        if (source.charAt(index) == 'l') {
                            estado = 20;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 20:
                        if (source.charAt(index) == 'o') {
                            estado = 13;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 21:
                        if (source.charAt(index) == 'l') {
                            tokens.add(new Token(tipoToken, "--", "--", linea));
                        }
                        estado = 0;
                        break;
                    case 22:
                        if (source.charAt(index) == 'r') {
                            tokens.add(new Token(tipoToken, "--", "--", linea));
                        }
                        estado = 0;
                        break;
                    case 23:
                        if (source.charAt(index) == 'u') {
                            estado = 24;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 24:
                        if (source.charAt(index) == 'l') {
                            estado = 25;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 25:
                        if (source.charAt(index) == 'o') {
                            tokens.add(new Token(tipoToken, "--", "--", linea));
                        }
                        estado = 0;
                        break;
                    case 26:
                        if (source.charAt(index) == 'i') {
                            estado = 27;
                        } else if (source.charAt(index) == 'u') {
                            estado = 35;
                            tipoToken = TipoToken.SUPER;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 27:
                        if (source.charAt(index) == '(') {
                            estado = 28;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 28:
                        if (source.charAt(index) == ')') {
                            tokens.add(new Token(tipoToken, "--", value, linea));
                            tokens.add(new Token(TipoToken.SI, "--", token, linea));
                            estado = 0;
                        } else {
                            token += currentCharacter;
                            if (source.charAt(index) == ' ') {
                                estado = 29;
                            } else {
                                value += currentCharacter;
                            }
                        }
                        break;
                    case 29:
                        if (source.charAt(index) == 'y') {
                            tipoToken = TipoToken.Y;
                            tokens.add(new Token(tipoToken, "--", value, linea));
                            value = "";
                        } else if (source.charAt(index) == 'o') {
                            tipoToken = TipoToken.O;
                            tokens.add(new Token(tipoToken, "--", value, linea));
                            value = "";
                        } else if (source.charAt(index) != ' ') {
                            value += currentCharacter;
                        }
                        estado = 28;
                        token += currentCharacter;
                        break;
                    case 30:
                        if (source.charAt(index) == 'e') {
                            estado = 31;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 31:
                        if (source.charAt(index) == 't') {
                            tokens.add(new Token(tipoToken, "--", value, linea));
                        }
                        estado = 0;
                        break;
                    case 32:
                        if (source.charAt(index) == 's') {
                            estado = 33;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 33:
                        if (source.charAt(index) == 't') {
                            estado = 34;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 34:
                        if (source.charAt(index) == 'e') {
                            tokens.add(new Token(tipoToken, "--", "--", linea));
                        }
                        estado = 0;
                        break;
                    case 35:
                        if (source.charAt(index) == 'p') {
                            estado = 36;
                        } else {
                            estado = 0;
                        }
                        break;
                    case 36:
                        if (source.charAt(index) == 'e') {
                            estado = 22;
                        } else {
                            estado = 0;
                        }
                        break;
                    default:
                        estado = 0;
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                break;
            }
            index += 1;
        }
        tokens.add(new Token(TipoToken.EOF, "--", null, linea));
        return tokens;
    }
}

/*
Signos o símbolos del lenguaje:
(
)
{
}
,
.
;
-
+
*
/
!
!=
=
==
<
<=
>
>=
// -> comentarios (no se genera token)
/* ... * / -> comentarios (no se genera token)
Identificador,
Cadena
Numero
Cada palabra reservada tiene su nombre de token

 */