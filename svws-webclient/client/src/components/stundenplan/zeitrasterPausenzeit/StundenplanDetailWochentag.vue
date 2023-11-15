<template>
	<svws-ui-content-card :title="Wochentag.fromIDorException(item.id).beschreibung">
		<div class="flex flex-wrap gap-1">
			<svws-ui-button v-for="s of fehlendeZeitraster" :key="s" type="secondary" @click="add(item, s)"> {{ s }}. Stunde einfügen </svws-ui-button>
			<svws-ui-button v-if="!fehlendeZeitraster.includes(neueStunde)" type="secondary" @click="add(item, neueStunde)"> {{ neueStunde }}. Stunde hinzufügen </svws-ui-button>
		</div>
		<div class="mt-3">
			<svws-ui-button type="danger" @click="removeWochentag"> <i-ri-delete-bin-line /> Wochentag entfernen </svws-ui-button>
			<span class="mt-1 opacity-50 inline-block text-error"> Achtung: Der Wochentag wird mit allen Stunden und Pausen entfernt.</span>
		</div>
		<!-- <svws-ui-button type="secondary" @click="addPausenzeit(item, s)"> {{ s }}. Stunde einfügen </svws-ui-button> -->
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanManager, StundenplanPausenzeit, StundenplanZeitraster} from "@core";
	import { Wochentag } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		item: Wochentag;
		stundenplanManager: () => StundenplanManager;
		removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
		addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
		addPausenzeit: (pausenzeit: StundenplanPausenzeit) => Promise<void>;
		removePausenzeiten: (pausenzeiten: Iterable<StundenplanPausenzeit>) => Promise<void>;
	}>();

	const neueStunde = computed<number>(()=> {
		const stunden = props.stundenplanManager().getListZeitrasterZuWochentag(props.item);
		const size = stunden.size();
		if (size === 0)
			return 1;
		return stunden.get(size - 1).unterrichtstunde + 1;
	})

	const fehlendeZeitraster = computed<number[]>(()=> {
		const arr = [];
		for (const s of props.stundenplanManager().zeitrasterGetStundenRange())
			if (props.stundenplanManager().zeitrasterGetByWochentagAndStundeOrNull(props.item.id, s) === null)
				arr.push(s);
		return arr;
	})

	async function add(w: Wochentag, stunde: number) {
		const list = props.stundenplanManager().zeitrasterGetDummyListe(w.id, w.id, stunde, stunde);
		await props.addZeitraster(list);
	}

	async function removeWochentag() {
		const zeitraster = props.stundenplanManager().getListZeitrasterZuWochentag(props.item);
		const pausenzeiten = props.stundenplanManager().pausenzeitGetMengeByWochentagOrEmptyList(props.item.id);
		await props.removeZeitraster(zeitraster);
		await props.removePausenzeiten(pausenzeiten);
	}
</script>
