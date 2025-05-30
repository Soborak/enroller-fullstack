import { useState, useEffect } from "react";
import NewMeetingForm from "./NewMeetingForm";


export default function MeetingsPage({ username }) {
    const [meetings, setMeetings] = useState([]);
    const [addingNewMeeting, setAddingNewMeeting] = useState(false);

    // Pobierz spotkania po załadowaniu komponentu
    useEffect(() => {
        fetch("/api/meetings")
            .then(res => res.json())
            .then(setMeetings)
            .catch(() => alert("Nie udało się pobrać spotkań."));
    }, []);

    // Dodawanie nowego spotkania
    async function handleNewMeeting(meeting) {
        const response = await fetch('/api/meetings', {
            method: 'POST',
            body: JSON.stringify(meeting),
            headers: { 'Content-Type': 'application/json' }
        });

        if (response.ok) {
            const updatedMeetings = await fetch("/api/meetings").then(res => res.json());
            setMeetings(updatedMeetings);
            setAddingNewMeeting(false);
            alert("Spotkanie zostało dodane!");
        } else {
            alert("Nie udało się dodać spotkania. Spróbuj ponownie!");
        }
    }

    // Usuwanie spotkania
    async function handleDeleteMeeting(id) {
        const response = await fetch(`/api/meetings/${id}`, { method: 'DELETE' });
        if (response.ok) {
            const updatedMeetings = await fetch("/api/meetings").then(res => res.json());
            setMeetings(updatedMeetings);
        } else {
            alert("Nie udało się usunąć spotkania.");
        }
    }

    return (
        <div>
            <h2>Zajęcia ({meetings.length})</h2>
            {addingNewMeeting ? (
                <NewMeetingForm onSubmit={handleNewMeeting} />
            ) : (
                <button onClick={() => setAddingNewMeeting(true)}>Dodaj nowe spotkanie</button>
            )}
            <table>
                <thead>
                <tr>
                    <th>Tytuł</th>
                    <th>Data</th>
                    <th>Opis</th>
                    <th>Akcje</th>
                </tr>
                </thead>
                <tbody>
                {meetings.map(meeting => (
                    <tr key={meeting.id}>
                        <td>{meeting.title}</td>
                        <td>{new Date(meeting.date).toLocaleDateString()}</td>
                        <td>{meeting.description}</td>
                        <td>
                            <button onClick={() => handleDeleteMeeting(meeting.id)}>Usuń</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
