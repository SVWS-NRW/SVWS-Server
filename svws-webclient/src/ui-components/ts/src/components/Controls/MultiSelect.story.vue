<script setup lang="ts">
import { hstEvent } from 'histoire/client';
import { ref } from 'vue';

import SvwsUiMultiSelect from "./MultiSelect.vue";

const items = [
  { id: 1, text: "item1" },
  { id: 2, text: "item2" },
  { id: 3, text: "item3" }
];

const modelValue1 = ref(items[0]);
const modelValue2 = ref();
const modelValue3 = ref();
const modelValue4 = ref();
const modelValue5 = ref([items[0]]);
const modelValue6 = ref([items[0]]);
const modelValue7 = ref();
const modelValue8 = ref([items[0]]);

function onInput(event: Event) {
  hstEvent("input", event);
}
</script>

<template>
  <Story title="SVWS UI/Controls/Multiselect">
    <Variant title="Simple">
      <svws-ui-multi-select
v-model="modelValue1" title="Simple" :item-text="(item) => item?.text || ''" :items="items"
        @input="onInput" />
    </Variant>

    <Variant title="Simple Sort">
      <svws-ui-multi-select
v-model="modelValue2" title="Simple" :items="items" :item-text="(item) => item?.text || ''"
        :item-sort="(a: any, b: any) => a.text + b.text" @input="onInput" />
    </Variant>

    <Variant title="Simple Text">
      <svws-ui-multi-select
v-model="modelValue3" title="Simple" :items="items" :item-text="(item: any) => item.id"
        :item-sort="(a: any, b: any) => a.text + b.text" @input="onInput" />
    </Variant>

    <Variant title="Simple Autocomplete">
      <svws-ui-multi-select
v-model="modelValue4" autocomplete title="SimpleAutocomplete" :items="items"
        :item-text="(item) => item?.text || ''"
        :item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
        @input="onInput" />
    </Variant>

    <Variant title="Tags">
      <svws-ui-multi-select
v-model="modelValue5" tags title="Tags" :item-text="(item) => item?.text || ''"
        :items="items" @input="onInput" />
    </Variant>

    <Variant title="TagsAutocomplete">
      <svws-ui-multi-select
v-model="modelValue6" :item-text="(item) => item?.text || ''" tags autocomplete
        title="TagsAutocomplete" :items="items" @input="onInput" />
    </Variant>

    <Variant title="SimpleHeadless">
      <svws-ui-multi-select
v-model="modelValue7" :item-text="(item) => item?.text || ''" headless title="SimpleHeadless"
        :items="items" @input="onInput" />
    </Variant>

    <Variant title="TagsHeadless">
      <svws-ui-multi-select
v-model="modelValue8" :item-text="(item) => item?.text || ''" tags autocomplete headless
        title="TagsHeadless" :items="items" @input="onInput" />
    </Variant>
  </Story>
</template>