<template>
	<div class="ui-grid-popover">
		<div v-if="!abc" class="flex flex-col w-fit gap-2">
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.SEHR_GUT_PLUS.ordinal() }" @click="tap(Note.SEHR_GUT_PLUS)">+</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.SEHR_GUT.ordinal() }" @click="tap(Note.SEHR_GUT)">1</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.SEHR_GUT_MINUS.ordinal() }" @click="tap(Note.SEHR_GUT_MINUS)">-</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.GUT_PLUS.ordinal() }" @click="tap(Note.GUT_PLUS)">+</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.GUT.ordinal() }" @click="tap(Note.GUT)">2</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.GUT_MINUS.ordinal() }" @click="tap(Note.GUT_MINUS)">-</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.BEFRIEDIGEND_PLUS.ordinal() }" @click="tap(Note.BEFRIEDIGEND_PLUS)">+</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.BEFRIEDIGEND.ordinal() }" @click="tap(Note.BEFRIEDIGEND)">3</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.BEFRIEDIGEND_MINUS.ordinal() }" @click="tap(Note.BEFRIEDIGEND_MINUS)">-</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.AUSREICHEND_PLUS.ordinal() }" @click="tap(Note.AUSREICHEND_PLUS)">+</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.AUSREICHEND.ordinal() }" @click="tap(Note.AUSREICHEND)">4</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.AUSREICHEND_MINUS.ordinal() }" @click="tap(Note.AUSREICHEND_MINUS)">-</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.MANGELHAFT_PLUS.ordinal() }" @click="tap(Note.MANGELHAFT_PLUS)">+</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.MANGELHAFT.ordinal() }" @click="tap(Note.MANGELHAFT)">5</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.MANGELHAFT_MINUS.ordinal() }" @click="tap(Note.MANGELHAFT_MINUS)">-</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item extra-button" @click="tap(Note.KEINE)"><span class="icon-xl i-ri-delete-back-2-line" /></span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.UNGENUEGEND.ordinal() }" @click="tap(Note.UNGENUEGEND)">6</span>
				<span class="popover-touch-item extra-button" @click="abc = true">abc</span>
			</div>
		</div>
		<div v-else class="flex flex-col w-fit gap-2">
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN.ordinal() }" @click="tap(Note.E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN)">E1</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.NICHT_BEURTEILT.ordinal() }" @click="tap(Note.NICHT_BEURTEILT)">NB</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.ABGEMELDET.ordinal() }" @click="tap(Note.ABGEMELDET)">AM</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.E2_MIT_ERFOLG_TEILGENOMMEN.ordinal() }" @click="tap(Note.E2_MIT_ERFOLG_TEILGENOMMEN)">E2</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.NICHT_TEILGENOMMEN.ordinal() }" @click="tap(Note.NICHT_TEILGENOMMEN)">NT</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.LEHRERMANGEL.ordinal() }" @click="tap(Note.LEHRERMANGEL)">LM</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.E3_TEILGENOMMEN.ordinal() }" @click="tap(Note.E3_TEILGENOMMEN)">E3</span>
				<span class="popover-touch-item" :class="{'bg-ui-selected': note.ordinal() === Note.ATTEST.ordinal() }" @click="tap(Note.ATTEST)">AT</span>
				<span class="popover-touch-item extra-button" @click="abc = false">123</span>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, watchEffect } from "vue";

	import { Note } from "@core/asd/types/Note";

	const note = defineModel<Note>({ default: () => Note.KEINE });
	const emit = defineEmits<{
		close: [val: void];
	}>();

	const abc = ref(false);
	watchEffect(() => abc.value = (note.value.ordinal() >= Note.ATTEST.ordinal()));

	function tap(val: Note) {
		if (val !== note.value) {
			note.value = val;
		}
	}

</script>
