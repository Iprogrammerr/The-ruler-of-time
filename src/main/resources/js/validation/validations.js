//This works only for latin based alphabets
export function Validations(minEmailNameLength = 3, minNameLength = 3, minPasswordLength = 8) {
    
    const _minEmailNameLength = minEmailNameLength;
    const _minEmailLength = _minEmailNameLength + 6;
    const _minNameLength = minNameLength;
    const _minPasswordLength = minPasswordLength;

    this.isNameValid = (name, allowSpaces = false) => {
        let trimmed = name.trim();
        let valid = trimmed.length >= _minNameLength;
        if (valid) {
            for (let c of trimmed) {
                if (!isLetter(c)) {
                    valid = (allowSpaces && c == ' ');
                    if (!valid) {
                        break;
                    }
                }
            }
        }
        return valid;
    };

    function isLetter(char) {
        return char.toUpperCase() != char.toLowerCase();
    };

    this.isEmailValid = (email) => {
        let valid = email.length >= _minEmailLength;
        if (valid) {
            let atIndex = email.indexOf('@');
            let dotIndex = email.indexOf('.');
            valid = atIndex > minEmailNameLength && (dotIndex - atIndex) > 2 && (email.length - dotIndex) > 2;   
        }
        return valid;
    };

    this.isPasswordValid = (password) => password.length >= _minPasswordLength;
    
    this.areInputsEqual = (first, second) => first.trim() === second.trim();
}