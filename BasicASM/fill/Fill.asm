@countblack
M=0
@countwhite
M=0
@SCREEN
D = A
@blacpix
M = D 
@whitepix
M = D
(keyboard)
    @keyboard
    @KBD
    D=M
    @press
    D;JGT

    @notpress
    D;JEQ
(press)
    @press
    @SCREEN 
    D = A 
    @whitepix
    M = D
    @blacpix
    A = M
    D = M
    @keyboard
    D; JNE

(turnblack)
    @turnblack
    @blacpix
    A = M 
    M = !M

    @countblack 
    M = M + 1

    @blacpix
    M = M + 1

    @KBD
    D = A
    @blacpix
    D = D - M 

    @keyboard
    D; JEQ
    
    @keyboard
    0;JMP

(notpress)
    @notpress
    @SCREEN 
    D = A 
    @blacpix
    M = D
    @countblack
    D = M
    @keyboard
    D;JEQ

    @whitepix
    A = M
    D = M
    @keyboard
    D;JEQ

(turnwhite)
    @turnwhite
    @whitepix
    A = M
    M = 0

    @whitepix
    M = M + 1

    @countblack
    M = M - 1

    @keyboard 
    0;JMP