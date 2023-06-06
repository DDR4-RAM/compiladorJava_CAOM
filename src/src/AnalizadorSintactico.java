package src;

import java.util.List;

public class AnalizadorSintactico {


    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
    }

    private final List<Token> tokens;

    private int i = 0;
    private boolean error = false;

    private Token preanalisis;

    private String errorMessage = "";

    public String parse() {
        System.out.println("[I] Inicia Parser...");
        i = 0;
        preanalisis = tokens.get(i);
        PROGRAM();
        if (!error && !preanalisis.tipo.equals(TipoToken.EOF)) {
            return "[X] Posicion " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo + " Metodo: parse()";
        } else if (!error && preanalisis.tipo.equals(TipoToken.EOF)) {
            return "Sin errores";
        }
        return errorMessage;
    }

    private void same(TipoToken tipo) {

        if (error) return;
        /*if (preanalisis.tipo.equals(TipoToken.COMENTARIO)) {
            System.out.print(preanalisis.tipo.toString());
            System.out.print(" = "+i+" = ");
            System.out.println(tipo);
            i++;
            preanalisis = tokens.get(i);
        }*/
        System.out.println("\nSAME** ");
        System.out.print(preanalisis.tipo.toString());
        System.out.print(" = " + i + " = ");
        System.out.print(tipo);

        if (preanalisis.tipo == tipo) {
            i++;
            preanalisis = tokens.get(i);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ". Se esperaba un  " + tipo + ". Metodo: same()";

        }
    }

    void PROGRAM() {
        if (preanalisis.tipo.equals(TipoToken.CLASE) || preanalisis.tipo.equals(TipoToken.FUN) || preanalisis.tipo.equals(TipoToken.VAR) || preanalisis.tipo.equals(TipoToken.NOT) ||
                preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) || preanalisis.tipo.equals(TipoToken.NULL) ||
                preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) || preanalisis.tipo.equals(TipoToken.ID) ||
                preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER) || preanalisis.tipo.equals(TipoToken.FOR) || preanalisis.tipo.equals(TipoToken.IF) ||
                preanalisis.tipo.equals(TipoToken.PRINT) || preanalisis.tipo.equals(TipoToken.RETURN) || preanalisis.tipo.equals(TipoToken.WHILE) || preanalisis.tipo.equals(TipoToken.LLAV_ABRE) || preanalisis.tipo.equals(TipoToken.COMENTARIO)) {
            DECLARATION();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ". Token " + preanalisis.tipo + ". Metodo: PROGRAM()";
        }
    }

    //DECLARACIONES

    void DECLARATION() {
        if (error) return;
        if (preanalisis.tipo.equals(TipoToken.CLASE)) {
            CLASS_DECL();
            DECLARATION();
        } else if (preanalisis.tipo.equals(TipoToken.FUN)) {
            FUN_DECL();
            DECLARATION();
        } else if (preanalisis.tipo.equals(TipoToken.VAR)) {
            VAR_DECL();
            DECLARATION();
        } else if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER) || preanalisis.tipo.equals(TipoToken.FOR) ||
                preanalisis.tipo.equals(TipoToken.IF) || preanalisis.tipo.equals(TipoToken.PRINT) || preanalisis.tipo.equals(TipoToken.RETURN) || preanalisis.tipo.equals(TipoToken.WHILE) ||
                preanalisis.tipo.equals(TipoToken.LLAV_ABRE)) {
            STATEMENT();
            DECLARATION();
        } else if (preanalisis.tipo.equals(TipoToken.COMENTARIO)) {
            i++;
            preanalisis = tokens.get(i);
            DECLARATION();
        }
    }

    void CLASS_DECL() {
        if (error) return;
        if (preanalisis.tipo.equals(TipoToken.CLASE)) {
            same(TipoToken.CLASE);
            same(TipoToken.ID);
            CLASS_INHER();
            same(TipoToken.LLAV_ABRE);
            FUNCTIONS();
            same(TipoToken.LLAV_CIERRE);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ". Se esperaba un  " + TipoToken.CLASE + ". Metodo: CLASS_DECL()";
        }
    }

    void CLASS_INHER() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.MENOR)) {
            same(TipoToken.MENOR);
            same(TipoToken.ID);
        }
    }

    void FUN_DECL() {
        if (error) return;
        if (preanalisis.tipo.equals(TipoToken.FUN)) {
            same(TipoToken.FUN);
            FUNCTION();
        } else {
            error = true;
            errorMessage = "[X] Posición:" + preanalisis.posicion + ".\nEsperado: " + TipoToken.FUN + ".\nMetodo: FUN_DECL()";
        }
    }

    void VAR_DECL() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.VAR)) {
            same(TipoToken.VAR);
            same(TipoToken.ID);
            VAR_INIT();
            same(TipoToken.PUNTO_COMA);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: " + TipoToken.ID + ".\nMetodo: VAR_DECL()";
        }
    }

    void VAR_INIT() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.ASIGNAR)) {
            same(TipoToken.ASIGNAR);
            EXPRESSION();
        }
    }

    //SENTENCIAS

    void STATEMENT() {
        if (error) return;
        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            EXPR_STMT();
        } else if (preanalisis.tipo.equals(TipoToken.FOR)) {
            FOR_STMT();
        } else if (preanalisis.tipo.equals(TipoToken.IF)) {
            IF_STMT();
        } else if (preanalisis.tipo.equals(TipoToken.PRINT)) {
            PRINT_STMT();
        } else if (preanalisis.tipo.equals(TipoToken.RETURN)) {
            RETURN_STMT();
        } else if (preanalisis.tipo.equals(TipoToken.WHILE)) {
            WHILE_STMT();
        } else if (preanalisis.tipo.equals(TipoToken.LLAV_ABRE)) {
            BLOCK();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: EXPRESSION.\nMetodo: STATEMENT()";
        }
    }

    void EXPR_STMT() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            EXPRESSION();
            same(TipoToken.PUNTO_COMA);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: EXPRESSION.\nMetodo: EXPR_STMT()";
        }
    }

    void FOR_STMT() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.FOR)) {
            same(TipoToken.FOR);
            same(TipoToken.PAR_ABRE);
            FOR_STMT_1();
            FOR_STMT_2();
            FOR_STMT_3();
            same(TipoToken.PAR_CIERRE);
            STATEMENT();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: " + TipoToken.FOR + ".\nMetodo: FOR_STMT()";
        }
    }

    void FOR_STMT_1() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.VAR)) {
            VAR_DECL();
        }
        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            EXPR_STMT();
        } else if (preanalisis.tipo.equals(TipoToken.PUNTO_COMA)) {
            same(TipoToken.PUNTO_COMA);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: " + TipoToken.PAR_ABRE + ".\nMetodo: FOR_STMT_1()";
        }
    }

    void FOR_STMT_2() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            EXPRESSION();
            same(TipoToken.PUNTO_COMA);
        } else if (preanalisis.tipo.equals(TipoToken.PUNTO_COMA)) {
            same(TipoToken.PUNTO_COMA);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: FOR_STMT_2()";
        }
    }

    void FOR_STMT_3() {
        if (error) return;
        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            EXPRESSION();
        }
    }

    void IF_STMT() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.IF)) {
            same(TipoToken.IF);
            same(TipoToken.PAR_ABRE);
            EXPRESSION();
            same(TipoToken.PAR_CIERRE);
            STATEMENT();
            ELSE_STATEMENT();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: " + TipoToken.IF + ".\nMetodo: IF_STMT()";
        }
    }

    void ELSE_STATEMENT() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.ELSE)) {
            same(TipoToken.ELSE);
            STATEMENT();
        }
    }

    void PRINT_STMT() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.PRINT)) {
            same(TipoToken.PRINT);
            EXPRESSION();
            same(TipoToken.PUNTO_COMA);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: " + TipoToken.PRINT + ".\nMetodo: PRINT_STMT()";
        }
    }

    void RETURN_STMT() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.RETURN)) {
            same(TipoToken.RETURN);
            RETURN_EXP_OPC();
            same(TipoToken.PUNTO_COMA);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: " + TipoToken.RETURN + ".\nMetodo: RETURN_STMT()";
        }
    }

    void RETURN_EXP_OPC() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            EXPRESSION();
        }
    }

    void WHILE_STMT() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.WHILE)) {
            same(TipoToken.WHILE);
            same(TipoToken.PAR_ABRE);
            EXPRESSION();
            same(TipoToken.PAR_CIERRE);
            STATEMENT();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: " + TipoToken.WHILE + ".\nMetodo: WHILE_STMT()";
        }
    }

    void BLOCK() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.LLAV_ABRE)) {
            same(TipoToken.LLAV_ABRE);
            BLOCK_DECL();
            same(TipoToken.LLAV_CIERRE);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando" + TipoToken.LLAV_ABRE + ".\nMetodo: BLOCK()";
        }
    }

    void BLOCK_DECL() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.CLASE) || preanalisis.tipo.equals(TipoToken.FUN) || preanalisis.tipo.equals(TipoToken.VAR) || preanalisis.tipo.equals(TipoToken.NOT) ||
                preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) || preanalisis.tipo.equals(TipoToken.NULL) ||
                preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) || preanalisis.tipo.equals(TipoToken.ID) ||
                preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER) || preanalisis.tipo.equals(TipoToken.FOR) || preanalisis.tipo.equals(TipoToken.IF) ||
                preanalisis.tipo.equals(TipoToken.PRINT) || preanalisis.tipo.equals(TipoToken.RETURN) || preanalisis.tipo.equals(TipoToken.WHILE) || preanalisis.tipo.equals(TipoToken.LLAV_ABRE)) {
            DECLARATION();
            BLOCK_DECL();
        }
    }

    //EXPRESIONES

    void EXPRESSION() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            ASSIGNMENT();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: EXPRESSION()";
        }
    }

    void ASSIGNMENT() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            LOGIC_OR();
            ASSIGNMENT_OPC();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: ASSIGNMENT()";
        }
    }

    void ASSIGNMENT_OPC() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.ASIGNAR)) {
            same(TipoToken.ASIGNAR);
            EXPRESSION();
        }
    }

    void LOGIC_OR() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            LOGIC_AND();
            ASSIGNMENT_OPC();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: LOGIC_OR()";
        }
    }

    void LOGIC_OR_2() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.OR)) {
            same(TipoToken.OR);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

    void LOGIC_AND() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            EQUALITY();
            LOGIC_AND_2();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: LOGIC_AND()";
        }
    }

    void LOGIC_AND_2() {
        if (error) return;
        if (preanalisis.tipo.equals(TipoToken.AND)) {
            same(TipoToken.AND);
            LOGIC_AND_2();
        }
    }

    void EQUALITY() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            COMPARISON();
            EQUALITY_2();
        }
    }

    void EQUALITY_2() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.DIFERENTE)) {
            same(TipoToken.DIFERENTE);
            COMPARISON();
            EQUALITY_2();
        } else if (preanalisis.tipo.equals(TipoToken.IGUAL)) {
            same(TipoToken.IGUAL);
            COMPARISON();
            EQUALITY_2();
        }
    }

    void COMPARISON() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            TERM();
            COMPARISON_2();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: COMPARISON()";
        }
    }

    void COMPARISON_2() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.MAYOR)) {
            same(TipoToken.MAYOR);
            TERM();
            COMPARISON_2();
        } else if (preanalisis.tipo.equals(TipoToken.MAYOR_IGUAL)) {
            same(TipoToken.MAYOR_IGUAL);
            TERM();
            COMPARISON_2();
        } else if (preanalisis.tipo.equals(TipoToken.MENOR)) {
            same(TipoToken.MENOR);
            TERM();
            COMPARISON_2();
        } else if (preanalisis.tipo.equals(TipoToken.MENOR_IGUAL)) {
            same(TipoToken.MENOR_IGUAL);
            TERM();
            COMPARISON_2();
        }
    }

    void TERM() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            FACTOR();
            TERM_2();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: TERM()";
        }
    }

    void TERM_2() {
        if (error) return;
        if (preanalisis.tipo.equals(TipoToken.MENOS)) {
            same(TipoToken.MENOS);
            FACTOR();
            TERM_2();
        } else if (preanalisis.tipo.equals(TipoToken.MAS)) {
            same(TipoToken.MAS);
            FACTOR();
            TERM_2();
        }
    }

    void FACTOR() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            UNARY();
            FACTOR_2();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: FACTOR()";
        }
    }

    void FACTOR_2() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.ENTRE)) {
            same(TipoToken.ENTRE);
            UNARY();
            FACTOR_2();
        } else if (preanalisis.tipo.equals(TipoToken.POR)) {
            same(TipoToken.POR);
            UNARY();
            FACTOR_2();
        }
    }

    void UNARY() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT)) {
            same(TipoToken.NOT);
            UNARY();
        } else if (preanalisis.tipo.equals(TipoToken.MENOS)) {
            same(TipoToken.MENOS);
            UNARY();
        } else if (preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            CALL();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: UNARY()";
        }
    }

    void CALL() {
        if (error) return;


        if (preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            PRIMARY();
            CALL_2();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: CALL()";
        }
    }

    void CALL_2() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.PAR_ABRE)) {
            same(TipoToken.PAR_ABRE);
            ARGUMENT_OPC();
            same(TipoToken.PAR_CIERRE);
            CALL_2();
        } else if (preanalisis.tipo.equals(TipoToken.PUNTO)) {
            same(TipoToken.PUNTO);
            same(TipoToken.ID);
            CALL_2();
        }
    }

    void CALL_OPC() {
        if (preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            CALL();
            same(TipoToken.PUNTO);
        }
    }

    void PRIMARY() {
        if (preanalisis.tipo.equals(TipoToken.TRUE)) {
            same(TipoToken.TRUE);
        } else if (preanalisis.tipo.equals(TipoToken.FALSE)) {
            same(TipoToken.FALSE);
        } else if (preanalisis.tipo.equals(TipoToken.NULL)) {
            same(TipoToken.NULL);
        } else if (preanalisis.tipo.equals(TipoToken.THIS)) {
            same(TipoToken.THIS);
        } else if (preanalisis.tipo.equals(TipoToken.NUMERO)) {
            same(TipoToken.NUMERO);
        } else if (preanalisis.tipo.equals(TipoToken.CADENA)) {
            same(TipoToken.CADENA);
        } else if (preanalisis.tipo.equals(TipoToken.ID)) {
            same(TipoToken.ID);
        } else if (preanalisis.tipo.equals(TipoToken.PAR_ABRE)) {
            same(TipoToken.PAR_ABRE);
            EXPRESSION();
            same(TipoToken.PAR_CIERRE);
        } else if (preanalisis.tipo.equals(TipoToken.SUPER)) {
            same(TipoToken.SUPER);
            same(TipoToken.PUNTO);
            same(TipoToken.ID);
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: PRIMARY()";
        }
    }

    void FUNCTION() {
        if (error) return;
        //System.out.println("XX"+preanalisis.tipo);
        if (preanalisis.tipo.equals(TipoToken.ID)) {
            //same(TipoToken.FUN);
            same(TipoToken.ID);
            same(TipoToken.PAR_ABRE);
            PARAMETERS_OPC();
            same(TipoToken.PAR_CIERRE);
            BLOCK();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: FUN.\nMetodo: FUNCTION()";
        }
    }

    void FUNCTIONS() {
        if (error) return;
        if (preanalisis.tipo.equals(TipoToken.ID)) {
            FUNCTION();
            FUNCTIONS();
        }
    }

    void PARAMETERS_OPC() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.ID)) {
            PARAMETERS();
        }
    }

    void PARAMETERS() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.ID)) {
            same(TipoToken.ID);
            PARAMETERS_2();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nEsperando: IDENTIFICADOR.\nMetodo: PARAMETERS()";
        }
    }

    void PARAMETERS_2() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.COMA)) {
            same(TipoToken.COMA);
            same(TipoToken.ID);
            PARAMETERS_2();
        }
    }

    void ARGUMENT_OPC() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            ARGUMENTS();
        }
    }

    void ARGUMENTS() {
        if (error) return;

        if (preanalisis.tipo.equals(TipoToken.NOT) || preanalisis.tipo.equals(TipoToken.MENOS) || preanalisis.tipo.equals(TipoToken.TRUE) || preanalisis.tipo.equals(TipoToken.FALSE) ||
                preanalisis.tipo.equals(TipoToken.NULL) || preanalisis.tipo.equals(TipoToken.THIS) || preanalisis.tipo.equals(TipoToken.NUMERO) || preanalisis.tipo.equals(TipoToken.CADENA) ||
                preanalisis.tipo.equals(TipoToken.ID) || preanalisis.tipo.equals(TipoToken.PAR_ABRE) || preanalisis.tipo.equals(TipoToken.SUPER)) {
            EXPRESSION();
            ARGUMENTS_2();
        } else {
            error = true;
            errorMessage = "[X] Posición " + preanalisis.posicion + ".\nMetodo: ARGUMENTS()";
        }
    }

    void ARGUMENTS_2() {
        if (error) return;
        if (preanalisis.tipo.equals(TipoToken.COMA)) {
            same(TipoToken.COMA);
            EXPRESSION();
            ARGUMENTS_2();
        }
    }


}
