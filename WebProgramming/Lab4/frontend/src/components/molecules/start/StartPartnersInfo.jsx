import React from "react";
import styles from "../css/start/StartPartnersInfo.module.scss";
import { SiOpenai } from "react-icons/si";
import { RiClaudeFill, RiGeminiFill, RiPerplexityFill } from "react-icons/ri";
import { GoCopilot } from "react-icons/go";

export const teams = [
    {
        id: 'chatgpt',
        name: 'ChatGPT',
        Logo: <SiOpenai size={30} />,
        href: "https://chatgpt.com/"
    },

    {
        id: 'claude',
        name: 'Claude',
        Logo: <RiClaudeFill size={30} />,
        href: "https://claude.com/"
    },

    {
        id: 'gemini',
        name: 'Gemini',
        Logo: <RiGeminiFill size={30} />,
        href: "https://gemini.google.com/",
    },

    {
        id: 'copilot',
        name: 'Copilot',
        Logo: <GoCopilot size={30} />,
        href: "https://github.com/copilot/"

    },

    {
        id: 'Perplexity',
        name: 'Perplexity',
        Logo: <RiPerplexityFill size={30}/>,
        href: "https://perplexity.ai/"
    }


];

function StartPartnersInfo(id) {
    return (
        <section id={id} className={styles.partners} aria-label="Our team of development">
            <p className={styles.caption}><strong>Наша команда разработки</strong></p>
            <ul className={styles.list}>
                {teams.map((team) => (
                    <li className={styles.item} key={team.id}>
                        <a href={team.href} className={styles.icon} aria-hidden="true">{team.Logo}</a>
                        <span>{team.name}</span>
                    </li>
                ))}
            </ul>
        </section>
    )
}

export default StartPartnersInfo
