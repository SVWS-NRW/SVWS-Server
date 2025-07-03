<template>
	<Story title="Card" id="ui-card" icon="ri:window-2-fill" :layout="{ type: 'single', iframe: false }" auto-props-disabled>
		<Variant title="Collapsible" id="Collapsible" :responsive-disabled="false">
			<div class="p-4">
				<ui-card :compact="state.compact" :is-open="state.isOpen" :collapsible="true" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" :title="state.title" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" @update:is-open="(isOpen) => state.isOpen = isOpen" />
			</div>
			<template #controls>
				<HstCheckbox v-model="state.compact" title="kompakt" />
				<HstCheckbox v-model="state.isOpen" title="geöffnet" />
				<div class="text-headline-sm pl-2">Header</div>
				<HstCheckbox v-model="state.showIcon" title="Icon" />
				<HstText v-model="state.title" title="Titel" />
				<HstText v-model="state.subtitle" title="Untertitel" />
				<HstText v-model="state.info" title="Info" />
				<HstRadio v-model="state.iconPosition" title="Collapse Position" :options="[
					{ label: 'rechts', value: 'right' },
					{ label: 'links', value: 'left' },
				]" />
				<div class="text-headline-sm pl-2">Body</div>
				<HstTextarea v-model="state.content" title="Inhalt" />
				<HstCheckbox v-model="state.showDivider" title="Trennlinie" />
				<HstTextarea v-model="state.footer" title="Footer" />
			</template>
		</Variant>
		<Variant title="Nicht Collapsible" id="NotCollapsible">
			<div class="p-4">
				<ui-card :compact="state.compact" :collapsible="false" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" :title="state.title" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" :is-open="state.isOpen" />
			</div>
			<template #controls>
				<HstCheckbox v-model="state.compact" title="kompakt" />
				<HstCheckbox v-model="state.isOpen" title="geöffnet" />
				<div class="text-headline-sm pl-2">Header</div>
				<HstCheckbox v-model="state.showIcon" title="Icon" />
				<HstText v-model="state.title" title="Titel" />
				<HstText v-model="state.subtitle" title="Untertitel" />
				<HstText v-model="state.info" title="Info" />
				<div class="text-headline-sm pl-2">Body</div>
				<HstTextarea v-model="state.content" title="Inhalt" />
				<HstCheckbox v-model="state.showDivider" title="Trennlinie" />
				<HstTextarea v-model="state.footer" title="Footer" />
			</template>
		</Variant>
		<Variant title="Collapsible Buttons" id="CollapsibleButtons">
			<div class="p-4">
				<ui-card :compact="state.compact" :is-open="state.isOpen" :collapsible="true" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" :title="state.title" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" :button-mode="state.buttonMode"
					:button-container="state.buttonContainer" :button-position="state.buttonPosition" :button-orientation="state.buttonOrientation"
					:on-edit="state.showEdit ? onEdit : undefined" :on-save="state.showSave ? onSave : undefined"
					:on-delete="state.showDelete ? onDelete : undefined" :on-cancel="state.showCancel ? onCancel : undefined"
					:edit-button-disabled="state.disabledEdit" :save-button-disabled="state.disabledSave" :delete-button-disabled="state.disabledDelete"
					:cancel-button-disabled="state.disabledCancel" :edit-button-disabled-reason="state.disabledReason"
					:save-button-disabled-reason="state.disabledReason" :delete-button-disabled-reason="state.disabledReason"
					:cancel-button-disabled-reason="state.disabledReason" @update:is-open="(isOpen) => state.isOpen = isOpen" />
			</div>
			<template #controls>
				<HstCheckbox v-model="state.compact" title="kompakt" />
				<HstCheckbox v-model="state.isOpen" title="geöffnet" />
				<div class="text-headline-sm pl-2">Header</div>
				<HstCheckbox v-model="state.showIcon" title="Icon" />
				<HstText v-model="state.title" title="Titel" />
				<HstText v-model="state.subtitle" title="Untertitel" />
				<HstText v-model="state.info" title="Info" />
				<HstRadio v-model="state.iconPosition" title="Collapse Position" :options="[
					{ label: 'rechts', value: 'right' },
					{ label: 'links', value: 'left' },
				]" />
				<div class="text-headline-sm pl-2">Body</div>
				<HstTextarea v-model="state.content" title="Inhalt" />
				<HstCheckbox v-model="state.showDivider" title="Trennlinie" />
				<HstTextarea v-model="state.footer" title="Footer" />
				<div class="text-headline-sm pl-2">Buttons</div>
				<HstRadio v-model="state.buttonMode" title="Modus" :options="[
					{ label: 'Icons', value: 'icon' },
					{ label: 'Texte', value: 'text' },
				]" />
				<HstRadio v-model="state.buttonContainer" title="Container" :options="[
					{ label: 'Content', value: 'content' },
					{ label: 'Footer', value: 'footer' },
				]" />
				<HstRadio v-model="state.buttonPosition" title="Position" :options="[
					{ label: 'Links', value: 'left' },
					{ label: 'Rechts', value: 'right' },
				]" />
				<HstRadio v-model="state.buttonOrientation" title="Orientierung" :options="[
					{ label: 'vertikal', value: 'vertical' },
					{ label: 'horizontal', value: 'horizontal' },
				]" />
				<div class="font-bold p-2">Anzeige</div>
				<HstCheckbox v-model="state.showSave" title="Speichern" />
				<HstCheckbox v-model="state.showCancel" title="Abbrechen" />
				<HstCheckbox v-model="state.showEdit" title="Bearbeiten" />
				<HstCheckbox v-model="state.showDelete" title="Löschen" />
				<div class="font-bold p-2">Disabled</div>
				<HstCheckbox v-model="state.disabledSave" title="Speichern" />
				<HstCheckbox v-model="state.disabledCancel" title="Abbrechen" />
				<HstCheckbox v-model="state.disabledEdit" title="Bearbeiten" />
				<HstCheckbox v-model="state.disabledDelete" title="Löschen" />
				<HstText v-model="state.disabledReason" title="Disabled Grund" />
			</template>
		</Variant>
		<Variant title="Validator Fehler" id="fehler" :responsive-disabled="false">
			<div class="p-4 grid gap-4">
				<ui-card :compact="state.compact" :is-open="state.isOpen" :collapsible="true" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" title="Kein Fehler" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" @update:is-open="(isOpen) => state.isOpen = isOpen"
					:fehler="ValidatorFehlerart.UNGENUTZT" />
				<ui-card :compact="state.compact" :is-open="state.isOpen" :collapsible="true" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" title="Hinweis" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" @update:is-open="(isOpen) => state.isOpen = isOpen"
					:fehler="ValidatorFehlerart.HINWEIS" />
				<ui-card :compact="state.compact" :is-open="state.isOpen" :collapsible="true" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" title="Kann Fehler" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" @update:is-open="(isOpen) => state.isOpen = isOpen"
					:fehler="ValidatorFehlerart.KANN" />
				<ui-card :compact="state.compact" :is-open="state.isOpen" :collapsible="true" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" title="Muss Fehler" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" @update:is-open="(isOpen) => state.isOpen = isOpen"
					:fehler="ValidatorFehlerart.MUSS" />
			</div>
			<template #controls>
				<HstCheckbox v-model="state.compact" title="kompakt" />
				<HstCheckbox v-model="state.isOpen" title="geöffnet" />
				<div class="text-headline-sm pl-2">Header</div>
				<HstCheckbox v-model="state.showIcon" title="Icon" />
				<HstText v-model="state.title" title="Titel" />
				<HstText v-model="state.subtitle" title="Untertitel" />
				<HstText v-model="state.info" title="Info" />
				<HstRadio v-model="state.iconPosition" title="Collapse Position" :options="[
					{ label: 'rechts', value: 'right' },
					{ label: 'links', value: 'left' },
				]" />
				<div class="text-headline-sm pl-2">Body</div>
				<HstTextarea v-model="state.content" title="Inhalt" />
				<HstCheckbox v-model="state.showDivider" title="Trennlinie" />
				<HstTextarea v-model="state.footer" title="Footer" />
			</template>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { reactive } from 'vue';
	import { ValidatorFehlerart } from '../../../../core/src/asd/validate/ValidatorFehlerart';

	const onEdit = () => alert("Bearbeiten");
	const onSave = () => alert("Speichern");
	const onDelete = () => alert("Löschen");
	const onCancel = () => alert("Abbrechen");

	const state = reactive({
		compact: false,
		isOpen: true,
		title: 'Card Title',
		subtitle: 'Card Subtitle',
		info: 'Info Text',
		content: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, ' +
			'sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum ' +
			'dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna ' +
			'aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus ' +
			'est Lorem ipsum dolor sit amet.',
		showIcon: true,
		iconPosition: 'right' as 'left' | 'right',
		footer: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed' +
			'diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren',
		showDivider: true,
		buttonMode: 'icon' as 'icon' | 'text',
		showEdit: true,
		showDelete: true,
		showCancel: false,
		showSave: false,
		buttonPosition: 'right' as 'left' | 'right',
		buttonContainer: 'content' as 'content' | 'footer',
		buttonOrientation: 'horizontal' as 'vertical' | 'horizontal',
		disabledEdit: false,
		disabledDelete: false,
		disabledCancel: false,
		disabledSave: false,
		disabledReason: 'Sie benötigen Adminrechte, um diese Aktion ausführen zu können',
	})

</script>
