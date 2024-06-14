<template>
	<div class="page--content">
		<div class="page--content-flex-column">
			Zum Ändern der Kalenderwochenzuordnungen bitte auf eine Kalenderwoche klicken. Diese und alle weiteren Zuordnungen werden dann eine Woche verschoben. Änderungen dementsprechend bitte von links nach rechts einpflegen.
			<div class="flex gap-1">
				<div v-for="kw of stundenplanManager().kalenderwochenzuordnungGetMengeAsList()" :key="kw.id" class="border flex flex-row p-2 cursor-pointer" @click="nextWochentyp(kw)">
					<div>{{ stundenplanManager().kalenderwochenzuordnungGetWocheAsString(kw) }}</div>
					<div class="font-bold size-6">{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(kw.wochentyp) }}</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanKalenderwochenProps } from "./SStundenplanKalenderwochenProps";
	import type { StundenplanKalenderwochenzuordnung } from "@core";
	import { ArrayList } from "@core";

	const props = defineProps<StundenplanKalenderwochenProps>();

	async function nextWochentyp(kw: StundenplanKalenderwochenzuordnung) {
		const kalenderwochenZuordnung = new ArrayList<StundenplanKalenderwochenzuordnung>();
		const modell = props.stundenplanManager().getWochenTypModell();
		for (const e of props.stundenplanManager().kalenderwochenzuordnungGetMengeAsList()) {
			if (e.kw >= kw.kw)
				e.wochentyp = (e.wochentyp + 1 > modell) ? 1 : e.wochentyp + 1;
			kalenderwochenZuordnung.add(e);
		}
		await props.patchKalenderwochenzuordnungen(kalenderwochenZuordnung);
	}

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 flex flex-row
	}

	.page--content-flex-column {
		@apply h-full overflow-y-auto w-full flex flex-col gap-8
	}

</style>
