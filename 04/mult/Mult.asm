    @i // i refers to some memory location 
    M=1  // put 1 in i
    @R2 
    M=0
(LOOP)
    @i
    D=M
    @R0
    D=D-M
    @END
    D;JGT

    @R1
    D=M 
    @R2
    M=M+D

    @i
    M=M+1

    @LOOP
    0;JMP
(END)
    @END
    0;JMP