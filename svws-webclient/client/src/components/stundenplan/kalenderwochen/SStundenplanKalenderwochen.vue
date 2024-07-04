<template>
	<div class="page--content">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<svws-ui-button @click="modus = !modus" title="Modus wechseln" type="transparent">
					<span :class="[modus ? 'icon-sm i-ri-play-line' : 'icon-sm i-ri-speed-line']" />
					Modus: <span>{{ modus ? 'Einzeln bearbeiten':'Fortlaufend bearbeiten' }}</span>
				</svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<svws-ui-table :items="zuordnungen" :columns="colsZuordnungen" clickable @update:clicked="toggleWochentyp" />
		<div>
			<svws-ui-table :items="summen" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { onMounted, ref, computed } from "vue";
	import type { StundenplanKalenderwochenProps } from "./SStundenplanKalenderwochenProps";
	import type { StundenplanKalenderwochenzuordnung } from "@core";
	import { ArrayList } from "@core";

	const props = defineProps<StundenplanKalenderwochenProps>();

	async function nextWochentyp(kw: StundenplanKalenderwochenzuordnung) {
		const kalenderwochenZuordnung = new ArrayList<StundenplanKalenderwochenzuordnung>();
		const modell = props.stundenplanManager().getWochenTypModell();
		const list = modus.value ? [kw] : props.stundenplanManager().kalenderwochenzuordnungGetMengeAsList();
		for (const e of list) {
			if (e.kw >= kw.kw)
				e.wochentyp = (e.wochentyp + 1 > modell) ? 1 : e.wochentyp + 1;
			kalenderwochenZuordnung.add(e);
		}
		await props.patchKalenderwochenzuordnungen(kalenderwochenZuordnung);
	}

	async function toggleWochentyp(value: { zuordnung: StundenplanKalenderwochenzuordnung, kalenderwoche: string, wochentyp: string }) {
		await nextWochentyp(value.zuordnung);
	}

	const modus = ref<boolean>(true);

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const summen = computed<Array<{ wochentyp: string, anzahl: number }>>(() => {
		const result : Array<{ wochentyp: string, anzahl: number }> = [];
		for (let wt = 1; wt <= props.stundenplanManager().getWochenTypModell(); wt++) {
			result.push({
				wochentyp: props.stundenplanManager().stundenplanGetWochenTypAsString(wt),
				anzahl: props.stundenplanManager().kalenderwochenzuordnungGetMengeByWochentyp(wt).size()
			});
		}
		return result;
	});

	const colsZuordnungen = [
		{ key: "kalenderwoche", label: "Kalenderwoche", span: 2 },
		{ key: "wochentyp", label: "Wochentyp", span: 1 },
	]

	const zuordnungen = computed<Array<{ zuordnung: StundenplanKalenderwochenzuordnung, kalenderwoche: string, wochentyp: string }>>(() => {
		const result : Array<{ zuordnung: StundenplanKalenderwochenzuordnung, kalenderwoche: string, wochentyp: string }> = [];
		for (const zuordnung of props.stundenplanManager().kalenderwochenzuordnungGetMengeAsList()) {
			result.push({
				zuordnung: zuordnung,
				kalenderwoche: props.stundenplanManager().kalenderwochenzuordnungGetWocheAsString(zuordnung),
				wochentyp: props.stundenplanManager().stundenplanGetWochenTypAsStringKurz(zuordnung.wochentyp)
			});
		}
		return result;
	});

</script>
