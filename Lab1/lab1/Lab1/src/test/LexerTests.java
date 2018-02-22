package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import frontend.Token;
import frontend.Token.Type;
import static frontend.Token.Type.*;

/**
 * This class contains unit tests for your lexer. Currently, there is only one test, but you
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {
	// helper method to run tests; no need to change this
	private final void runtest(String input, Token... output) {
		Lexer lexer = new Lexer(new StringReader(input));
		int i=0;
		Token actual=new Token(MODULE, 0, 0, ""), expected;
		try {
			do {
				assertTrue(i < output.length);
				expected = output[i++];
				try {
					actual = lexer.nextToken();
					assertEquals(expected, actual);
				} catch(Error e) {
					if(expected != null)
						fail(e.getMessage());
					/* return; */
				}
			} while(!actual.isEOF());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/** Example unit test. */
	@Test
	public void testKWs() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("module false return while true import if else public type void break",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 0, 13, "return"),
				new Token(WHILE, 0, 20, "while"),
				new Token(TRUE, 0, 26, "true"),
				new Token(IMPORT, 0 , 31, "import"),
				new Token(IF,0,38,"if"),
				new Token (ELSE , 0 , 41,"else"),
				new Token(PUBLIC, 0 , 46,"public"),
				new Token(TYPE, 0, 53, "type"),
				new Token(VOID, 0, 58,"void"),
				new Token(BREAK, 0 , 63,"break"),
				new Token(EOF, 0, 68, ""));
	}

	@Test
	public void testStringLiteralWithDoubleQuote() {
		runtest("\"\"\"",
				new Token(STRING_LITERAL, 0, 0, ""),
				(Token)null,
				new Token(EOF, 0, 3, ""));
	}

	@Test
	public void testStringLiteral() {
		runtest("\"\\n\"", 
				new Token(STRING_LITERAL, 0, 0, "\\n"),
				new Token(EOF, 0, 4, ""));
	}

	@Test
	public void testBoolean() {
		runtest("boolean", 
				new Token(BOOLEAN, 0, 0, "boolean"),
				new Token(EOF, 0, 7, ""));
	}
	@Test
	public void testIntLiteral() {
		runtest("5000", 
				new Token(INT_LITERAL, 0, 0, "5000"),
				new Token(EOF, 0, 4, ""));
	}
	@Test
	public void testId() {
		runtest("apple123", 
				new Token(ID, 0, 0, "apple123"),
				new Token(EOF, 0, 8, ""));
	}
	
	@Test
	public void testEQEQ() {
		runtest("==", 
				new Token(EQEQ, 0, 0, "=="),
				new Token(EOF, 0, 2, ""));
	}
	
	
	@Test
	public void testIDEqlInt_Lit() {
		runtest("apple = 45", 
				new Token(ID, 0, 0, "apple"),
				new Token(EQL,0,6,"="),
				new Token(INT_LITERAL, 0,8,"45"),
				new Token(EOF, 0, 10, ""));
	}
	
	@Test
	public void testKeyword() {
		runtest("Break", 
				new Token(Type.ID, 0, 0, "Break"),
				new Token(EOF, 0, 5, ""));
	}	
	
	@Test
	public void testOperators() {
		runtest("/ * + - = == != >= > <= <", 
				new Token(DIV, 0, 0, "/"),
				new Token(TIMES, 0, 2, "*"),
				new Token(PLUS, 0, 4, "+"),
				new Token(MINUS, 0, 6, "-"),
				new Token(EQL, 0, 8, "="),
				new Token(EQEQ, 0, 10, "=="),
				new Token(NEQ, 0, 13, "!="),
				new Token(GEQ, 0, 16, ">="),
				new Token(GT, 0, 19, ">"),
				new Token(LEQ, 0, 21, "<="),
				new Token(LT, 0, 24, "<"),
				new Token(EOF, 0, 25, ""));
	}
	
	@Test
	public void specialCase() {
		runtest("if(apple==banana){\n apple=45;}", 
				new Token(IF, 0, 0, "if"),
				new Token(LPAREN, 0, 2, "("),
				new Token(ID, 0, 3, "apple"),
				new Token(EQEQ, 0, 8, "=="),
				new Token(ID, 0, 10, "banana"),
				new Token(RPAREN, 0,16,")"),
				new Token(LCURLY, 0, 17, "{"),
				new Token(ID, 1, 1, "apple"),
				new Token(EQL, 1, 6, "="),
				new Token(INT_LITERAL, 1, 7, "45"),
				new Token(SEMICOLON, 1, 9, ";"),
				new Token(RCURLY, 1, 10, "}"),
				new Token(EOF, 1, 11, ""));
	}
	
	@Test
	public void specialCase2() {
		runtest("int[] array = {45,55,66};", 
				new Token(INT, 0, 0, "int"),
				new Token(LBRACKET, 0, 3, "["),
				new Token(RBRACKET, 0, 4, "]"),
				new Token(ID, 0, 6, "array"),
				new Token(EQL, 0, 12, "="),
				new Token(LCURLY, 0,14,"{"),
				new Token(INT_LITERAL, 0, 15, "45"),
				new Token(COMMA, 0,17, ","),
				new Token(INT_LITERAL, 0, 18, "55"),
				new Token(COMMA, 0, 20, ","),
				new Token(INT_LITERAL, 0, 21, "66"),
				new Token(RCURLY, 0, 23, "}"),
				new Token(SEMICOLON,0,24,";"),
				new Token(EOF, 0, 25, ""));
	}
	
	@Test
	public void testPunctuation() {
		runtest(", [ { ( ] } ) ;", 
				new Token(COMMA, 0, 0, ","),
				new Token(LBRACKET, 0, 2, "["),
				new Token(LCURLY, 0, 4, "{"),
				new Token(LPAREN, 0, 6, "("),
				new Token(RBRACKET, 0, 8, "]"),
				new Token(RCURLY, 0, 10, "}"),
				new Token(RPAREN, 0, 12, ")"),
				new Token(SEMICOLON, 0, 14, ";"),
				new Token(EOF, 0, 15, ""));
	}
	
	@Test
	public void testIfElse() {
		runtest("if(true){return;}else{return;}", 
				new Token(Type.IF, 0, 0, "if"),
				new Token(Type.LPAREN, 0, 2, "("),
				new Token(Type.TRUE, 0, 3, "true"),
				new Token(Type.RPAREN, 0, 7, ")"),
				new Token(Type.LCURLY, 0, 8, "{"),
				new Token(Type.RETURN, 0, 9, "return"),
				new Token(Type.SEMICOLON, 0, 15, ";"),
				new Token(Type.RCURLY, 0, 16, "}"),
				new Token(Type.ELSE, 0, 17, "else"),
				new Token(Type.LCURLY, 0, 21, "{"),
				new Token(Type.RETURN, 0, 22, "return"),
				new Token(Type.SEMICOLON, 0, 28, ";"),
				new Token(Type.RCURLY, 0, 29, "}"),
				new Token(EOF, 0, 30, ""));
	}

}
