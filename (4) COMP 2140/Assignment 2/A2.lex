import java.io.*;
%%
%{      public static int identifiers, keywords, numbers, comments, quotedString = 0;
        public static void main(String argv[]) throws java.io.IOException {
        A2 yy = new A2(new FileReader("A2_1.tiny"));
	    while (yy.yylex()>=0){}
        PrintWriter writer = new PrintWriter(new FileWriter("A2.output"));
        String message = "identifiers: " + identifiers + "\nkeywords: " + keywords + "\nnumbers: " + numbers + "\ncomments: " + comments + "\nquotedString: " + quotedString;
        writer.println(message);
        writer.close();
        }
%}
%notunix
%integer
%type void
%class A2

%state COMMENT
%state QUOTED
NUMBER=[0-9]+(\.[0-9]+)?
KEYWORD="WRITE"|"READ"|"IF"|"ELSE"|"RETURN"|"BEGIN"|"END"|"MAIN"|"STRING"|"INT"|"REAL"
IDENTIFIER=[a-zA-Z][a-zA-Z0-9]* 

%%
<YYINITIAL>"/*" {yybegin(COMMENT);}
<YYINITIAL>"\"" {yybegin(QUOTED);}
<COMMENT>"*/" {comments++; yybegin(YYINITIAL);}
<QUOTED>"\"" {quotedString++; yybegin(YYINITIAL);}
<YYINITIAL>{NUMBER} {numbers++;}
<YYINITIAL>{KEYWORD} {keywords++;}
<YYINITIAL>{IDENTIFIER} {identifiers++;}
.|\r|\n {}