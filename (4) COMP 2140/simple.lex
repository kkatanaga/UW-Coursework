%%
%{    public static void main(String argv[]) throws java.io.IOException {
	  MyLexer yy = new MyLexer(System.in);
	  while (true){
	     yy.yylex();
        }
      }
%}
%notunix
%type void
%class MyLexer
%eofval{ return;
%eofval}

IDENTIFIER = [a-zA-Z_][a-zA-Z0-9_]*

%%
"int" { System.out.println("INT recognized");}
{IDENTIFIER} { System.out.println("ID is ..." + yytext());}
\r|\n {}
. {}