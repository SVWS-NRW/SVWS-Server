<template>
	<svws-ui-modal v-model:show="show" size="small" class="hidden">
		<template #modalTitle>
			Benutzergruppe hinzufügen
		</template>

		<template #modalContent>
			<svws-ui-input-wrapper center>
				<svws-ui-text-input v-model="bezeichnung" type="text" placeholder="Bezeichnung" />
				<svws-ui-checkbox type="toggle" v-model="inputbgIstAdmin"> Admin-Rechte </svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="create"> Weiter </svws-ui-button>
		</template>
	</svws-ui-modal>

	<svws-ui-button type="trash" :disabled="!showDeleteIcon" @click="deleteBenutzergruppen" />

	<svws-ui-button type="icon" @click="show = true" :has-focus>
		<span class="icon i-ri-add-line" />
	</svws-ui-button>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const props = withDefaults(defineProps<{
		showDeleteIcon: boolean;
		createBenutzergruppe : (bezeichnung: string, istAdmin: boolean) => Promise<void>;
		deleteBenutzergruppen : () => Promise<void>;
		hasFocus?: boolean;
	}>(), {
		hasFocus: false,
	});

	const show = ref<boolean>(false);

	const bezeichnung = ref();
	const inputbgIstAdmin=ref(false);

	function create(){
		void props.createBenutzergruppe(bezeichnung.value,inputbgIstAdmin.value);
		show.value = false;
		bezeichnung.value = "";
		inputbgIstAdmin.value=false;
	}

</script>
