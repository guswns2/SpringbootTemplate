@REM 암호화
@REM mvn jasypt:encrypt-value -Djasypt.encryptor.password="암/복호화에 사용할 비밀번호" -Djasypt.plugin.value="암호화할 값" 
mvn jasypt:encrypt-value -Djasypt.encryptor.password="b7878370-f5a6-4f9c-9aca-741253f71455" -Djasypt.plugin.value="jhj9615@"

@REM 복호화
@REM mvn jasypt:decrypt-value -Djasypt.encryptor.password="암/복호화에 사용할 비밀번호" -Djasypt.plugin.value="복호화할 값" 
mvn jasypt:decrypt-value -Djasypt.encryptor.password="b7878370-f5a6-4f9c-9aca-741253f71455" -Djasypt.plugin.value="bVwVAoOMXehxiGdULxK54rZevwTkPzEJ2CDVeMu/KEKLwGuC/24sXS5ClP2frXMm"
