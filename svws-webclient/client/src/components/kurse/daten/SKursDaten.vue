<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<template #actions>
				<svws-ui-checkbox v-model="istSichtbar"> Ist sichtbar </svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :model-value="data().kuerzel" @change="kuerzel=>patch({ kuerzel })" type="text" />
				<svws-ui-select title="Lehrer" v-model="lehrer" :items="mapLehrer.values()" :item-text="l => l.kuerzel" />
				<svws-ui-select title="Fach" :model-value="mapFaecher.get(data().idFach)"
					@update:model-value="value => patch({ idFach: value?.id ?? -1 })"
					:items="mapFaecher" :item-text="(f: FaecherListeEintrag) => f.kuerzel + ' (' + f.bezeichnung + ')'" />
				<svws-ui-select title="Kursart" :items="kursarten.keys()" :item-text="k => k + ' (' + (kursarten.get(k) ?? '???') + ')'"
					:model-value="data().kursartAllg" @update:model-value="value => patch({ kursartAllg: value ?? '' })" />
				<svws-ui-input-number placeholder="Wochenstunden" :model-value="data().wochenstunden" @change="wstd => patch({ wochenstunden: wstd ?? 0 })" />
				<svws-ui-multi-select title="Jahrgänge" v-model="jahrgaenge" :items="mapJahrgaenge" :item-text="jg => jg?.kuerzel ?? ''" />
				<svws-ui-input-number placeholder="Sortierung" :model-value="data().sortierung" @change="sortierung=> sortierung && patch({ sortierung })" />
				<svws-ui-text-input placeholder="Zeugnisbezeichnung" :model-value="data().bezeichnungZeugnis" @change="b => patch({ bezeichnungZeugnis : b })" type="text" />
				<svws-ui-select title="Fortschreibungsart" :model-value="KursFortschreibungsart.fromID(data().idKursFortschreibungsart)"
					@update:model-value="value => patch({ idKursFortschreibungsart: value?.id ?? 0 })"
					:items="KursFortschreibungsart.values()" :item-text="f => f.beschreibung" />
				<svws-ui-multi-select title="Schienen" v-model="schienen" :items="Array.from({length: 40}, (_, i) => i + 1)" :item-text="s => 'Schiene ' + s" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Kursliste">
			<svws-ui-table :columns="colsSchueler" :items="data().schueler">
				<template #cell(status)="{ value }: { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ SchuelerStatus.fromID(value)?.bezeichnung || "" }}</span>
				</template>
				<template #header(linkToSchueler)>
					<i-ri-group-line />
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
						<i-ri-link />
					</button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FaecherListeEintrag, JahrgangsListeEintrag, LehrerListeEintrag } from "@core";
	import { SchuelerStatus, ZulaessigeKursart, KursFortschreibungsart, ArrayList } from "@core";
	import type { DataTableColumn } from "@ui";
	import type { KursDatenProps } from "./SKursDatenProps";

	const props = defineProps<KursDatenProps>();

	const jahrgaenge = computed<JahrgangsListeEintrag[]>({
		get: () => {
			const arr = [];
			for (const id of props.data().idJahrgaenge) {
				const e = props.mapJahrgaenge.get(id);
				if (e !== undefined)
					arr.push(e);
			}
			return arr;
		},
		set: (value) => {
			const result = new ArrayList<number>();
			value.forEach(j => result.add(j.id));
			void props.patch({ idJahrgaenge: result });
		}
	});

	const schienen = computed<number[]>({
		get: () => {
			return props.data().schienen.toArray(new Array<number>);
		},
		set: (value) => {
			const result = new ArrayList<number>();
			let changed = false;
			for (const s of value) {
				if (!props.data().schienen.contains(s))
					changed = true;
				result.add(s);
			}
			if (!changed)
				changed = (props.data().schienen.size() !== result.size());
			if (changed)
				void props.patch({ schienen : result });
		}
	});

	const lehrer = computed<LehrerListeEintrag | undefined>({
		get: () => {
			const idLehrer = props.data().lehrer;
			return (idLehrer === null) ? undefined : props.mapLehrer.get(idLehrer);
		},
		set: (value) => void props.patch({ lehrer: value === undefined ? null : value.id })
	});

	const kursarten = computed<Map<string, string>>(() => {
		const arten = new Map<string, string>();
		for (const art of ZulaessigeKursart.get(props.schulform)) {
			if (art.daten.kuerzel === "PUK")
				continue;
			if ((art.daten.kuerzelAllg !== null) && (art.daten.bezeichnungAllg !== null))
				arten.set(art.daten.kuerzelAllg, art.daten.bezeichnungAllg);
			else
				arten.set(art.daten.kuerzel, art.daten.bezeichnung);
			if (art.daten.kuerzelAllg === "DK")
				arten.set(art.daten.kuerzel, art.daten.bezeichnung);
		}
		return new Map([...arten.entries()].sort());
	});

	const istSichtbar = computed<boolean>({
		get: () => props.data === undefined ? false : props.data().istSichtbar,
		set: (value) => void props.patch({ istSichtbar: value })
	});

	const colsSchueler: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 }
	];

</script>
