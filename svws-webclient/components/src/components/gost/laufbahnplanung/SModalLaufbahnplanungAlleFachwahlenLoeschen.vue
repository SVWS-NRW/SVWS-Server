<template>
	<div>
		<svws-ui-button @click="toggle_modal" size="small" type="transparent" class="hover--danger">
			<i-ri-delete-bin-line />
			<template v-if="hatFesteWahlen">Nicht feste Schüler-Fachwahlen zurücksetzen</template>
			<template v-else>Alle Schüler-Fachwahlen zurücksetzen</template>
		</svws-ui-button>
		<svws-ui-modal :show="showModal" size="medium" type="danger">
			<template #modalTitle>
				<template v-if="hatFesteWahlen">Nicht feste Fachwahlen aller Schüler im Abiturjahrgang auf die Vorlage zurücksetzen</template>
				<template v-else>Die Fachwahlen aller Schüler im Abiturjahrgang auf die Vorlage zurücksetzen</template>
			</template>
			<template #modalDescription>
				<div class="flex gap-1 mb-2">
					<template v-if="hatFesteWahlen">
						Sollen für alle Schüler des Abiturjahrgangs die nicht festen Fachwahlen der noch in Planung befindlichen Halbjahre
						gelöscht und auf die Vorlage zurückgesetzt werden?
					</template>
					<template v-else>
						Sollen für alle Schüler des Abiturjahrgangs die Fachwahlen auf die Vorlage zurückgesetzt werden?
					</template>
				</div>
			</template>
			<template #modalActions>
				<svws-ui-button @click="toggle_modal" type="secondary">Abbrechen</svws-ui-button>
				<svws-ui-button @click="reset_fachwahlen" type="danger">Ja</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import { type GostJahrgangsdaten } from '@core';


	const props = defineProps<{
		gostJahrgangsdaten: () => GostJahrgangsdaten;
		resetFachwahlen: () => Promise<void>;
	}>();

	const hatFesteWahlen = computed<boolean>(() => {
		const jg = props.gostJahrgangsdaten().jahrgang;
		return (jg === "Q1") || (jg == "Q2") || ((jg == "EF") && (props.gostJahrgangsdaten().istBlockungFestgelegt[0]));
	});

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	function toggle_modal() {
		showModal().value = !showModal().value;
	}

	async function reset_fachwahlen() {
		showModal().value = false;
		await props.resetFachwahlen();
	}

</script>
