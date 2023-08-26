<template>
	<svws-ui-content-card>
		<svws-ui-input-wrapper :grid="4">
			<div class="text-headline-md text-left">
				<span>{{ Wochentag.fromIDorException(item.id) }}</span>
			</div>
		</svws-ui-input-wrapper>
		<svws-ui-button type="danger" @click="removeWochentag"> {{ item.beschreibung }} mit allen Stunden und Pausen entfernen </svws-ui-button>
		<svws-ui-button v-for="s of fehlendeZeitraster" :key="s" type="secondary" @click="addZeitraster(item, s)"> {{ s }}. Stunde einfügen </svws-ui-button>
		<svws-ui-button v-if="!fehlendeZeitraster.includes(neueStunde)" type="secondary" @click="addZeitraster(item, neueStunde)"> {{ neueStunde }}. Stunde einfügen </svws-ui-button>
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
		addZeitraster: (wochentag: Wochentag | undefined, stunde : number | undefined) => Promise<void>;
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

	async function removeWochentag() {
		const zeitraster = props.stundenplanManager().getListZeitrasterZuWochentag(props.item);
		const pausenzeiten = props.stundenplanManager().pausenzeitGetMengeByWochentagOrEmptyList(props.item.id);
		await props.removeZeitraster(zeitraster);
		await props.removePausenzeiten(pausenzeiten);
	}
</script>
