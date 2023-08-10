<template>
	<div class="stundenplan h-5/6" :class="cols">
		<div class="stundenplan-cell"> &ndash; </div>
		<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="stundenplan-cell bg-svws-300">
			<div class="flex justify-between"> <span>{{ wochentag.beschreibung }}</span> <i-ri-delete-bin-line class="cursor-pointer" /></div>
		</div>
		<div v-if="manager().zeitrasterGetWochentageAlsEnumRange().length < 7" @click="addWochentag" class="cursor-pointer"><i-ri-add-line /></div>
		<div class="stundenplan-cell relative">
			<div v-for="stunde in manager().zeitrasterGetStundenRange()" :key="stunde" class="bg-yellow-200 absolute w-48 rounded-md"
				:style="posZeitraster(undefined, stunde)">
				<div class="flex justify-between">
					<div class="flex content-start">
						<SvwsUiTextInput :model-value="stunde" @update:model-value="patchStunde" headless style="width: 1rem;" /><span>.&nbsp;Stunde</span>
					</div>
					<i-ri-delete-bin-line class="cursor-pointer" />
				</div>
				<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten">
					{{ zeiten }}
				</div>
			</div>
			<div class="absolute cursor-pointer" style="top: 100%" @click="addStunde"><i-ri-add-line /></div>
		</div>
		<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="relative">
			<template v-for="stunde in manager().getListZeitrasterZuWochentag(wochentag)" :key="stunde">
				<div class="absolute h-full w-full bg-yellow-100 rounded-md" :style="posZeitraster(wochentag, stunde.unterrichtstunde)">
					<div class="flex justify-between">
						<div class="flex content-start">
							<SvwsUiTextInput :model-value="manager().zeitrasterGetByIdStringOfUhrzeitBeginn(stunde.id)" @update:model-value="patchAnfang" headless style="width: 3rem;" /> -&nbsp;
							<SvwsUiTextInput :model-value="manager().zeitrasterGetByIdStringOfUhrzeitEnde(stunde.id)" @update:model-value="patchEnde" headless style="width: 4rem;" />
						</div> <i-ri-delete-bin-line class="cursor-pointer" />
					</div>
				</div>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanManager, StundenplanZeitraster } from "@core";
	import { Wochentag } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		manager: () => StundenplanManager;
		patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
		addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

	const beginn = computed(() => {
		// TODO Pausenzeiten Min
		return props.manager().zeitrasterGetMinutenMin();
	});

	const ende = computed(() => {
		// TODO Pausenzeiten Max
		return props.manager().zeitrasterGetMinutenMax();
	});

	const gesamtzeit = computed(() => {
		const tmp = ende.value - beginn.value;
		return tmp <= 0 ? 360 : tmp;
	});


	function posZeitraster(wochentag: Wochentag | undefined, stunde: number): string {
		// TODO Ermittle die Info aus allen vorhanden Wochentagen (!) - nicht nur vom Montag
		const w = wochentag === undefined ? Wochentag.MONTAG : wochentag;
		const z = props.manager().zeitrasterGetByWochentagAndStundeOrException(w.id, stunde);
		let top = 0;
		let height = 10;
		if ((z.stundenbeginn === null) || (z.stundenende === null)) {
			const sb = props.manager().zeitrasterGetStundeMin();
			const se = props.manager().zeitrasterGetStundeMax();
			const stunden = se - sb + 1;
			top = ((stunde - sb) / stunden) * 100;
			height = 100 / stunden;
		} else {
			top = ((z.stundenbeginn - beginn.value) / gesamtzeit.value) * 100;
			height = ((z.stundenende - z.stundenbeginn) / gesamtzeit.value) * 100;
		}
		return "top: " + top + "%; height: " + height + "%;";
	}

	const cols = computed(() => {
		const colcount = props.manager().zeitrasterGetWochentageAlsEnumRange().length;
		const result = {
			'stundenplan-col1': colcount === 1,
			'stundenplan-col2': colcount === 2,
			'stundenplan-col3': colcount === 3,
			'stundenplan-col4': colcount === 4,
			'stundenplan-col5': colcount === 5,
			'stundenplan-col6': colcount === 6,
			'stundenplan-col7': colcount === 7,
		};
		return result;
	});

	async function addWochentag() {
		const curr = props.manager().zeitrasterGetWochentagMaxEnum().id;
		console.log("Wochentag hinzufügen: ", Wochentag.fromIDorException(curr+1).beschreibung)
	}

	async function addStunde() {
		const curr = props.manager().zeitrasterGetStundeMax();
		console.log("Stunde hinzufügen: ", curr+1)
	}

	async function patchAnfang(zeit: string) {
		console.log(zeit)
	}
	async function patchEnde(zeit: string) {
		console.log(zeit)
	}
	async function patchStunde(stunde: number) {
		console.log(stunde)
	}
</script>

<style lang="postcss" scoped>

	.stundenplan {
		@apply grid gap-4 rounded-md select-none;
		grid-template-rows: 1.6rem 1fr
	}

	.stundenplan-col1 {
		grid-template-columns: 12rem repeat(2, minmax(0, 1fr))
	}

	.stundenplan-col2 {
		grid-template-columns: 12rem repeat(3, minmax(0, 1fr))
	}

	.stundenplan-col3 {
		grid-template-columns: 12rem repeat(4, minmax(0, 1fr))
	}

	.stundenplan-col4 {
		grid-template-columns: 12rem repeat(5, minmax(0, 1fr))
	}

	.stundenplan-col5 {
		grid-template-columns: 12rem repeat(6, minmax(0, 1fr))
	}

	.stundenplan-col6 {
		grid-template-columns: 12rem repeat(7, minmax(0, 1fr))
	}

	.stundenplan-col7 {
		grid-template-columns: 12rem repeat(8, minmax(0, 1fr))
	}

	.stundenplan-cell {
		@apply text-center rounded-md w-full h-full
	}

</style>