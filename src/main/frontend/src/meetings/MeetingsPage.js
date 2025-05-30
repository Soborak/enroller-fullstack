import {useState} from "react";
import NewMeetingForm from "./NewMeetingForm";
import MeetingsList from "./MeetingsList";

export default function MeetingsPage({username}) {
    const [meetings, setMeetings] = useState([]);
    const [addingNewMeeting, setAddingNewMeeting] = useState(false);

    // function handleNewMeeting(meeting) {
    //     const nextMeetings = [...meetings, meeting];
    //     setMeetings(nextMeetings);
    //     setAddingNewMeeting(false);
    // }
    async function handleNewMeeting(meeting) {
        // 1. Wyślij żądanie POST do backendu
        const response = await fetch('/api/meetings', {
            method: 'POST',
            body: JSON.stringify(meeting),
            headers: { 'Content-Type': 'application/json' }
        });

        // 2. Sprawdź, czy backend potwierdził dodanie
        if (response.ok) {
            // 3. Pobierz nową listę spotkań z backendu (masz pewność, że dane są aktualne)
            const updatedMeetings = await fetch("/api/meetings").then(res => res.json());
            setMeetings(updatedMeetings);
            setAddingNewMeeting(false);
            alert("Spotkanie zostało dodane!");
        } else {
            // 4. Obsłuż błąd
            alert("Nie udało się dodać spotkania. Spróbuj ponownie!");
        }
    }

    function handleDeleteMeeting(meeting) {
        const nextMeetings = meetings.filter(m => m !== meeting);
        setMeetings(nextMeetings);
    }

    return (
        <div>
            <h2>Zajęcia ({meetings.length})</h2>
            {
                addingNewMeeting
                    ? <NewMeetingForm onSubmit={(meeting) => handleNewMeeting(meeting)}/>
                    : <button onClick={() => setAddingNewMeeting(true)}>Dodaj nowe spotkanie</button>
            }
            {meetings.length > 0 &&
                <MeetingsList meetings={meetings} username={username}
                              onDelete={handleDeleteMeeting}/>}
        </div>
    )
}
