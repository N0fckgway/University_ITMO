import React from "react";
import styles from "./css/StartPartnersInfo.module.scss";
import { Claude, Copilot, DeepSeek, Gemini, OpenAI } from "@lobehub/icons";

export const teams = [
    {
        id: 'chatgpt',
        name: 'ChatGPT',
        Icon: OpenAI
    },

    {
        id: 'claude',
        name: 'Claude',
        Icon: Claude.Color
    },

    {
        id: 'gemini',
        name: 'Gemini',
        Icon: Gemini.Color
    },

    {
        id: 'copilot',
        name: 'Copilot',
        Icon: Copilot.Color
    },

    {
        id: 'deepseek',
        name: 'DeepSeek',
        Icon: DeepSeek.Color
    }


];

function StartPartnersInfo() {
    return (
        <section className={styles.partners} aria-label="Our team of development">
            <p className={styles.caption}><strong>Наша команда разработки</strong></p>
            <ul className={styles.list}>
                {teams.map((team) => (
                    <li className={styles.item} key={team.id}>
                        <team.Icon size={56} />
                        <span>{team.name}</span>
                    </li>
                ))}
            </ul>
        </section>
    )
}

export default StartPartnersInfo
