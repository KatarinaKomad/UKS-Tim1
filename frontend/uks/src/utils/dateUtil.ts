export const MY_DATE_FORMAT = {
    parse: {
        dateInput: 'YYYY-MM-DD', // this is how your date will be parsed from Input
    },
    display: {
        dateInput: 'DD.MM.YYYY.', // this is how your date will get displayed on the Input
        monthYearLabel: 'MMMM YYYY',
        dateA11yLabel: 'LL',
        monthYearA11yLabel: 'MMMM YYYY'
    }
};

export const dateToString = (date: Date): string => {
    return date.toISOString().slice(0, 19);

}