<template>
	<svws-ui-content-card :title="`${selected}. Stunde`">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(first.stundenbeginn ?? 0)" required placeholder="Stundenbeginn" @change="value => start = value " />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(first.stundenende ?? 0)" placeholder="Stundenende" @change="value => ende = value" />
			<svws-ui-button v-if="!disabled && !ueberschneidung" type="secondary" @click="patchZeiten"> Stundenzeiten aktualisieren </svws-ui-button>
			<div v-if="ueberschneidung" class="text-error">Die Änderung der Stundenzeiten würde zu einer Überschneidung führen und ist nicht zulässig.</div>
			<div class="col-span-full">
				<svws-ui-input-number :model-value="selected" required placeholder="Bezeichnung" @change="patchStunde" ref="numberInput" :min="0" :max="15" />
			</div>
			<div class="col-span-full">
				<svws-ui-button v-for="w of fehlendeZeitraster" :key="w.id" type="secondary" class="mb-2 w-52" @click="add(w, selected)">{{ w.kuerzel }} {{ selected }}. Stunde einfügen </svws-ui-button>
			</div>
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removeZeitraster(stundenplanManager().getListZeitrasterZuStunde(props.selected))" class="w-52"> <span class="icon i-ri-delete-bin-line" /> Stunde entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import { SvwsUiInputNumber } from "@ui";
	import type { StundenplanManager, StundenplanPausenzeit, Wochentag } from "@core";
	import { ArrayList, DateUtils, StundenplanZeitraster } from "@core";

	const props = defineProps<{
		selected: number;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (zeitraster : Iterable<StundenplanZeitraster>) => Promise<void>;
		removeZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
		addZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
		setSelection: (value: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) => void;
	}>();

	const numberInput = ref<ComponentExposed<typeof SvwsUiInputNumber>>();

	const first = computed(() => {
		if (props.stundenplanManager().getListZeitrasterZuStunde(props.selected).isEmpty())
			return new StundenplanZeitraster();
		return props.stundenplanManager().getListZeitrasterZuStunde(props.selected).get(0);
	})

	const start = ref<string>(DateUtils.getStringOfUhrzeitFromMinuten(first.value.stundenbeginn ?? 0));
	const ende = ref<string>(DateUtils.getStringOfUhrzeitFromMinuten(first.value.stundenende ?? 0));

	watch(() => props.selected, () => {
		start.value = DateUtils.getStringOfUhrzeitFromMinuten(first.value.stundenbeginn ?? 0);
		ende.value = DateUtils.getStringOfUhrzeitFromMinuten(first.value.stundenende ?? 0);
	})

	const disabled = computed<boolean>(() => (DateUtils.getStringOfUhrzeitFromMinuten(first.value.stundenbeginn ?? 0) === start.value) && (DateUtils.getStringOfUhrzeitFromMinuten(first.value.stundenende ?? 0) === ende.value));

	const ueberschneidung = computed<boolean>(() => {
		const list = new ArrayList<StundenplanZeitraster>();
		for (const wt of props.stundenplanManager().zeitrasterGetWochentageAlsEnumRange()) {
			const zeitraster = new StundenplanZeitraster();
			zeitraster.wochentag = wt.id;
			zeitraster.stundenbeginn = DateUtils.gibMinutenOfZeitAsString(start.value);
			zeitraster.stundenende = DateUtils.gibMinutenOfZeitAsString(ende.value);
			zeitraster.unterrichtstunde = props.selected;
			list.add(zeitraster);
		}
		const ignoreList = props.stundenplanManager().getListZeitrasterZuStunde(props.selected);
		console.log(list.toArray(), ignoreList.toArray())
		return props.stundenplanManager().zeitrasterGetSchneidenSichListeMitIgnore(list, ignoreList);
	})

	async function patchStunde(stunde: number | null) {
		if ((stunde === null) || (stunde < 0) || (stunde > 14))
			return;
		const list = new ArrayList<StundenplanZeitraster>();
		for (const zeitraster of props.stundenplanManager().getListZeitrasterZuStunde(props.selected)) {
			if (!props.stundenplanManager().zeitrasterExistsByWochentagAndStunde(zeitraster.wochentag, stunde)) {
				zeitraster.unterrichtstunde = stunde;
				list.add(zeitraster);
			}
		}
		if (list.isEmpty())
			numberInput.value?.reset();
		else {
			await props.patchZeitraster(list);
			props.setSelection(stunde);
		}
	}

	async function patchZeiten() {
		const list = new ArrayList<StundenplanZeitraster>();
		for (const zeitraster of props.stundenplanManager().getListZeitrasterZuStunde(props.selected)) {
			const stundenbeginn = DateUtils.gibMinutenOfZeitAsString(start.value);
			const stundenende = DateUtils.gibMinutenOfZeitAsString(ende.value);
			zeitraster.stundenbeginn = stundenbeginn;
			zeitraster.stundenende = stundenende;
			list.add(zeitraster);
		}
		await props.patchZeitraster(list);
	}

	async function add(w: Wochentag, stunde: number) {
		const list = props.stundenplanManager().zeitrasterGetDummyListe(w.id, w.id, stunde, stunde);
		await props.addZeitraster(list);
	}

	const fehlendeZeitraster = computed<Wochentag[]>(()=> {
		const arr = [];
		for (const w of props.stundenplanManager().zeitrasterGetWochentageAlsEnumRange())
			if (props.stundenplanManager().zeitrasterGetByWochentagAndStundeOrNull(w.id, props.selected) === null)
				arr.push(w);
		return arr;
	})

</script>
