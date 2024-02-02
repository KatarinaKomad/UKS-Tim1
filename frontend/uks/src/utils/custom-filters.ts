import { FormInput } from "src/models/forms/input"

export const formFilter = (formInputs: FormInput[], id: string): any => {
    return formInputs.filter(input => input.id === id).at(0)?.control.value
}


export const areArraysEqual = (array1: { id: any }[], array2: { id: any }[]): boolean => {
    if (array1?.length === array2?.length) {
        return array1.every(element1 => {

            for (let index = 0; index < array2.length; index++) {
                if (element1.id === array2[index].id) {
                    return true;
                }
            }
            return false
        });
    }

    return false;
}